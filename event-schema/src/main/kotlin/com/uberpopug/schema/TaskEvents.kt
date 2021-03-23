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
    val eventId: String = UUID.randomUUID().toString(),
    val eventTime: OffsetDateTime = now(),
    val producer: String = "task-service"
)

class TaskCreated(payload: TaskData) : TaskDomainEvent<TaskData>(payload, "task-created", 1)

class TaskAssigned(payload: TaskData) : TaskDomainEvent<TaskData>(payload, "task-assigned", 1)

class TaskCompleted(payload: TaskData) : TaskDomainEvent<TaskData>(payload, "task-completed", 1)

class TaskClosed(payload: TaskData) : TaskDomainEvent<TaskData>(payload, "task-closed", 1)

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
