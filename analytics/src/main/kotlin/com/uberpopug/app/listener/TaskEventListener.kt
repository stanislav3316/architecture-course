package com.uberpopug.app.listener

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.uberpopug.app.data.RawEventRepository
import com.uberpopug.app.data.task.Task
import com.uberpopug.app.data.task.TaskRepository
import com.uberpopug.schema.AppEvent
import com.uberpopug.schema.TaskAssigned
import com.uberpopug.schema.TaskClosed
import com.uberpopug.schema.TaskCompleted
import com.uberpopug.schema.TaskCreated
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class TaskEventListener(
    private val objectMapper: ObjectMapper,
    private val taskRepository: TaskRepository,
    private val rawEventRepository: RawEventRepository
) {
    private val log = LoggerFactory.getLogger(TaskEventListener::class.java)

    @KafkaListener(
        topics = ["task", "task-stream"],
        groupId = "analytics"
    )
    fun onDomainEvent(@Payload event: String, acknowledgment: Acknowledgment) {

        try {

            val taskEvent = objectMapper.readValue<AppEvent>(event)
            rawEventRepository.save(taskEvent)

            when (taskEvent) {

                is TaskCreated -> {
                    val task = Task(
                        taskId = taskEvent.taskId,
                        title = taskEvent.title,
                        description = taskEvent.description,
                        assignedToEmployeeId =  taskEvent.assignedToEmployeeId,
                        createdByEmployeeId = taskEvent.createdByEmployeeId,
                        status = taskEvent.status,
                        createdAt = taskEvent.createdAt,
                        completedTaskValue = null,
                        assignedTaskValue = null
                    )
                    taskRepository.save(task)
                }

                is TaskAssigned -> {
                    taskRepository.updateAssignedEmployeeId(
                        taskId = taskEvent.taskId,
                        assignedEmployeeId = taskEvent.assignedToEmployeeId
                    )
                }

                is TaskCompleted -> {
                    taskRepository.updateStatus(
                        taskId = taskEvent.taskId,
                        status = "COMPLETED"
                    )
                }

                is TaskClosed -> {
                    taskRepository.updateStatus(
                        taskId = taskEvent.taskId,
                        status = "CLOSED"
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
