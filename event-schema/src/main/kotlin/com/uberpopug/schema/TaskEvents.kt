package com.uberpopug.schema

import java.time.OffsetDateTime

class TaskCreated(
    eventProducer: String,
    val taskId: String,
    val title: String,
    val description: String,
    val assignedToEmployeeId: String?,
    val createdByEmployeeId: String,
    val status: String,
    val createdAt: OffsetDateTime
) : AppEvent(
    meta = EventMetaData("task-created", 1, eventProducer)
)

class TaskAssigned(
    eventProducer: String,
    val taskId: String,
    val assignedToEmployeeId: String
) : AppEvent(
    meta = EventMetaData("task-assigned", 1, eventProducer)
)

class TaskCompleted(
    eventProducer: String,
    val taskId: String
) : AppEvent(
    meta = EventMetaData("task-completed", 1, eventProducer)
)

class TaskClosed(
    eventProducer: String,
    val taskId: String
) : AppEvent(
    meta = EventMetaData("task-closed", 1, eventProducer)
)
