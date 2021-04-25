package com.uberpopug.accounting.publisher

import com.fasterxml.jackson.databind.ObjectMapper
import com.uberpopug.accounting.asAccountOpenedEvent
import com.uberpopug.accounting.asTaskPriceEstimatedEvent
import com.uberpopug.accounting.domain.Account
import com.uberpopug.accounting.streaming.task.Task
import com.uberpopug.schema.AppEvent
import com.uberpopug.schema.EmployeeDayClosed
import com.uberpopug.schema.PayedForAssignedTask
import com.uberpopug.schema.PayedForCompletedTask
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class AccountingPublisher(
    private val kafkaTemplate: KafkaTemplate<Any, Any>,
    private val mapper: ObjectMapper
) {
    private val businessTopic = "accounting"
    private val streamTopic = "accounting-stream"

    fun onNewAccount(account: Account) {
        val accountId = account.accountId!!
        val serializedEvent = serializeEvent(account.asAccountOpenedEvent())
        kafkaTemplate.send(streamTopic, accountId, serializedEvent)
    }

    fun onPayForCompletedTask(account: Account, completedTaskValue: BigDecimal, taskId: String, transactionId: String) {
        val accountId = account.accountId!!
        val serializedEvent = serializeEvent(
            PayedForCompletedTask(
                "accounting",
                account.accountId!!,
                completedTaskValue,
                taskId,
                transactionId
            )
        )
        kafkaTemplate.send(streamTopic, accountId, serializedEvent)
    }

    fun onPayForAssignedTask(account: Account, assignTaskValue: BigDecimal, taskId: String, transactionId: String) {
        val accountId = account.accountId!!
        val serializedEvent = serializeEvent(
            PayedForAssignedTask(
                "accounting",
                accountId,
                assignTaskValue,
                taskId,
                transactionId
            )
        )
        kafkaTemplate.send(streamTopic, accountId, serializedEvent)
    }

    fun onCloseEmployeesDay(account: Account, transactionId: String) {
        val accountId = account.accountId!!
        val serializedEvent = serializeEvent(
            EmployeeDayClosed(
                "accounting",
                account.employeeId,
                account.accountId!!,
                account.amount,
                transactionId
            )
        )
        kafkaTemplate.send(businessTopic, accountId, serializedEvent)
        kafkaTemplate.send(streamTopic, accountId, serializedEvent)
    }

    fun onTaskEstimated(task: Task) {
        val taskId = task.taskId
        val serializedEvent = serializeEvent(task.asTaskPriceEstimatedEvent())
        kafkaTemplate.send(streamTopic, taskId, serializedEvent)
    }

    private fun serializeEvent(event: AppEvent) = mapper.writeValueAsString(event)
}
