package com.uberpopug.accounting.listener.task

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
class TaskEventListener(
    private val employeeHandlers: List<AppEventHandler>,
    private val objectMapper: ObjectMapper
) {
    private val log = LoggerFactory.getLogger(TaskEventListener::class.java)

    @KafkaListener(
        topics = ["task"],
        groupId = "accounting-service"
    )
    fun onDomainEvent(@Payload event: String, acknowledgment: Acknowledgment) {

        try {
            val taskEvent = objectMapper.readValue<AppEvent>(event)

            val availableHandlers = employeeHandlers.filter { handler -> handler.isSuitableFor(taskEvent) }
            availableHandlers.forEach { handler -> handler.handle(taskEvent) }

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
