package com.uberpopug.app.domain.task

import java.time.OffsetDateTime
import java.time.OffsetDateTime.now
import java.util.UUID

abstract class TaskDomainEvent<T>(
    val payload: T,
    val eventName: String,
    val eventId: String = UUID.randomUUID().toString(),
    val eventVersion: Int = 1,
    val eventTime: OffsetDateTime = now(),
    val producer: String = "task-service"
)

class TaskCreated(payload: Task) : TaskDomainEvent<Task>(payload, "task-created")

class TaskAssigned(payload: Task) : TaskDomainEvent<Task>(payload, "task-assigned")

class TaskCompleted(payload: Task) : TaskDomainEvent<Task>(payload, "task-completed")

class TaskClosed(payload: Task) : TaskDomainEvent<Task>(payload, "task-closed")
