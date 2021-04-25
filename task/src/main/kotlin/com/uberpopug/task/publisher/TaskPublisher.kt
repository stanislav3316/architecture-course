package com.uberpopug.task.publisher

import com.fasterxml.jackson.databind.ObjectMapper
import com.uberpopug.app.asTaskAssignedEvent
import com.uberpopug.app.asTaskClosedEvent
import com.uberpopug.app.asTaskCompletedEvent
import com.uberpopug.app.asTaskCreatedEvent
import com.uberpopug.schema.AppEvent
import com.uberpopug.task.domain.Task
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class TaskPublisher(
    private val kafkaTemplate: KafkaTemplate<Any, Any>,
    private val mapper: ObjectMapper
) {
    private val businessTopic = "task"
    private val streamTopic = "task-stream"

    fun onCreate(task: Task) {
        val taskId = task.taskId!!
        val serializedEvent = serializeEvent(task.asTaskCreatedEvent())
        kafkaTemplate.send(businessTopic, taskId, serializedEvent)
        kafkaTemplate.send(streamTopic, taskId, serializedEvent)
    }

    fun onAssign(task: Task) {
        val taskId = task.taskId!!
        val serializedEvent = serializeEvent(task.asTaskAssignedEvent())
        kafkaTemplate.send(businessTopic, taskId, serializedEvent)
        kafkaTemplate.send(streamTopic, taskId, serializedEvent)
    }

    fun onComplete(task: Task) {
        val taskId = task.taskId!!
        val serializedEvent = serializeEvent(task.asTaskCompletedEvent())
        kafkaTemplate.send(businessTopic, taskId, serializedEvent)
        kafkaTemplate.send(streamTopic, taskId, serializedEvent)
    }

    fun onClose(task: Task) {
        val taskId = task.taskId!!
        val serializedEvent = serializeEvent(task.asTaskClosedEvent())
        kafkaTemplate.send(streamTopic, taskId, serializedEvent)
    }

    private fun serializeEvent(event: AppEvent): String = mapper.writeValueAsString(event)
}

//todo: logs !
