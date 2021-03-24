package com.uberpopug.accounting.listener.employee

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.uberpopug.schema.AppEvent
import com.uberpopug.schema.AppEventHandler
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class EmployeeEventListener(
    private val employeeHandlers: List<AppEventHandler>,
    private val objectMapper: ObjectMapper
) {
    private val log = LoggerFactory.getLogger(EmployeeEventListener::class.java)

    @KafkaListener(
        topics = ["employee-aggregate"],
        groupId = "accounting-service"
    )
    fun onDomainEvent(@Payload event: String, acknowledgment: Acknowledgment) {

        try {
            val employeeEvent = objectMapper.readValue<AppEvent>(event)

            val availableHandlers = employeeHandlers.filter { handler -> handler.isSuitableFor(employeeEvent) }
            availableHandlers.forEach { handler -> handler.handle(employeeEvent) }

            if (availableHandlers.isEmpty()) {
                log.warn("no suitable handler for - {}", event)
            }

        } catch (e: Exception) {
            log.error("cannot deserialize employee event - {}", event)
            //todo: publish to dead letters topic
        }

        acknowledgment.acknowledge()
    }
}
