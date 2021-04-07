package com.uberpopug.app.listener

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.uberpopug.app.data.RawEventRepository
import com.uberpopug.app.data.accounting.Account
import com.uberpopug.app.data.accounting.AccountingRepository
import com.uberpopug.app.data.accounting.Transaction
import com.uberpopug.app.data.task.TaskRepository
import com.uberpopug.schema.AccountOpened
import com.uberpopug.schema.AppEvent
import com.uberpopug.schema.EmployeeDayClosed
import com.uberpopug.schema.PayedForAssignedTask
import com.uberpopug.schema.PayedForCompletedTask
import com.uberpopug.schema.TaskPriceEstimated
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class AccountingEventListener(
    private val objectMapper: ObjectMapper,
    private val accountingRepository: AccountingRepository,
    private val taskRepository: TaskRepository,
    private val rawEventRepository: RawEventRepository
) {
    private val log = LoggerFactory.getLogger(EmployeeEventListener::class.java)

    private val systemAccountId = "-1"

    @KafkaListener(
        topics = ["accounting", "accounting-stream"],
        groupId = "analytics"
    )
    fun onDomainEvent(@Payload event: String, acknowledgment: Acknowledgment) {

        try {

            val accountingEvent = objectMapper.readValue<AppEvent>(event)
            rawEventRepository.save(accountingEvent)

            when (accountingEvent) {

                is AccountOpened -> {
                    val account = Account(
                        accountId = accountingEvent.accountId,
                        employeeId = accountingEvent.employeeId,
                        createdAt = accountingEvent.accountCreatedAt,
                        lastClosedDay = null
                    )
                    accountingRepository.saveAccount(account)
                }

                is PayedForAssignedTask -> {
                    val transaction = Transaction(
                        transactionId = accountingEvent.transactionId,
                        amount = accountingEvent.amount,
                        toAccount = systemAccountId,
                        fromAccount = accountingEvent.accountId,
                        type = "PAY_FOR_ASSIGNED_TASK",
                        taskId = accountingEvent.taskId,
                        createdAt = accountingEvent.meta.eventTime
                    )
                    accountingRepository.saveTransaction(transaction)
                }

                is PayedForCompletedTask -> {
                    val transaction = Transaction(
                        transactionId = accountingEvent.transactionId,
                        amount = accountingEvent.amount,
                        toAccount = accountingEvent.accountId,
                        fromAccount = systemAccountId,
                        type = "PAY_FOR_COMPLETED_TASK",
                        taskId = accountingEvent.taskId,
                        createdAt = accountingEvent.meta.eventTime
                    )
                    accountingRepository.saveTransaction(transaction)
                }

                is EmployeeDayClosed -> {
                    val transaction = Transaction(
                        transactionId = accountingEvent.transactionId,
                        amount = accountingEvent.amount,
                        toAccount = systemAccountId,
                        fromAccount = accountingEvent.accountId,
                        type = "CLOSED_EMPLOYEE_DAY",
                        taskId = null,
                        createdAt = accountingEvent.meta.eventTime
                    )
                    accountingRepository.saveTransaction(transaction)
                }

                is TaskPriceEstimated -> {
                    taskRepository.updateEstimatedTask(
                        taskId = accountingEvent.taskId,
                        completedTaskValue = accountingEvent.completedTaskValue,
                        assignedTaskValue = accountingEvent.assignedTaskValue
                    )
                }
            }

        } catch (e: Exception) {
            log.error("cannot deserialize accounting event - {}", event)
            //todo: publish to dead letters topic
        }

        acknowledgment.acknowledge()
    }
}
