package com.uberpopug.schema

import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.OffsetDateTime
import java.time.OffsetDateTime.now
import java.util.UUID

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, property = "type")
sealed class TaskDomainEvent<T>(
    val payload: T,
    val eventName: String,
    val eventVersion: Int,
    val producer: String,
    val eventId: String = UUID.randomUUID().toString(),
    val eventTime: OffsetDateTime = now()
)

class TaskCreated(
    payload: TaskData,
    producer: String
) : TaskDomainEvent<TaskData>(payload, "task-created", 1, producer)

class TaskAssigned(
    payload: TaskData,
    producer: String
) : TaskDomainEvent<TaskData>(payload, "task-assigned", 1, producer)

class TaskCompleted(
    payload: TaskData,
    producer: String
) : TaskDomainEvent<TaskData>(payload, "task-completed", 1, producer)

class TaskClosed(
    payload: TaskData,
    producer: String
) : TaskDomainEvent<TaskData>(payload, "task-closed", 1, producer)

// класс для данных о Task, который шарится в либе и доступен для версии v1 в событиях
data class TaskData(
    val taskId: String,
    val title: String,
    val description: String,
    val assignedToEmployeeId: String?,
    val createdByEmployeeId: String,
    val status: String,
    val createdAt: OffsetDateTime
)
