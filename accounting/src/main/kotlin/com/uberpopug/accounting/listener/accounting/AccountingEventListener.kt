package com.uberpopug.accounting.listener.accounting

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.uberpopug.accounting.notification.NotificationService
import com.uberpopug.schema.AppEvent
import com.uberpopug.schema.EmployeeDayClosed
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class AccountingEventListener(
    private val objectMapper: ObjectMapper,
    private val notificationService: NotificationService
) {
    private val log = LoggerFactory.getLogger(AccountingEventListener::class.java)

    @KafkaListener(
        topics = ["accounting"],
        groupId = "accounting-service"
    )
    fun onDomainEvent(@Payload event: String, acknowledgment: Acknowledgment) {

        try {

            when (val accountingEvent = objectMapper.readValue<AppEvent>(event)) {

                is EmployeeDayClosed -> {
                    //todo: try using background processing as example man
                    notificationService.notifyWithClosedDay(
                        employeeId = accountingEvent.employeeId,
                        accountId = accountingEvent.accountId,
                        amount = accountingEvent.amount
                    )
                }

                else -> log.debug("accounting event - $accountingEvent was ignored")
            }

        } catch (e: Exception) {
            log.error("cannot deserialize accounting event - {}", event)
            //todo: publish to dead letters topic
        }

        acknowledgment.acknowledge()
    }
}
