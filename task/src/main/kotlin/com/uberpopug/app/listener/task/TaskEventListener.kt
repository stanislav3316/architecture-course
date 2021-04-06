package com.uberpopug.app.listener.task

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.uberpopug.app.notification.NotificationService
import com.uberpopug.schema.AppEvent
import com.uberpopug.schema.TaskAssigned
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class TaskEventListener(
    private val objectMapper: ObjectMapper,
    private val notificationService: NotificationService
) {
    private val log = LoggerFactory.getLogger(TaskEventListener::class.java)

    @KafkaListener(
        topics = ["task"],
        groupId = "task-service"
    )
    fun onDomainEvent(@Payload event: String, acknowledgment: Acknowledgment) {

        try {

            when (val taskEvent = objectMapper.readValue<AppEvent>(event)) {
                is TaskAssigned -> {
                    notificationService.notifyWithAssignedTask(
                        employeeId = taskEvent.assignedToEmployeeId,
                        taskId = taskEvent.taskId
                    )
                }

                else -> log.debug("event - $taskEvent was ignored")
            }

        } catch (e: Exception) {
            log.error("cannot deserialize task event - {}", event)
            //todo: publish to dead letters topic
        }

        acknowledgment.acknowledge()
    }
}
