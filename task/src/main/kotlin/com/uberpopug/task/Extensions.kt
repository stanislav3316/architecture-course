package com.uberpopug.app

import com.uberpopug.task.domain.Task
import com.uberpopug.schema.TaskAssigned
import com.uberpopug.schema.TaskClosed
import com.uberpopug.schema.TaskCompleted
import com.uberpopug.schema.TaskCreated

fun Task.asTaskCreatedEvent() = TaskCreated(
    eventProducer = "task-service",
    taskId = this.taskId!!,
    title = this.title,
    description = this.description,
    assignedToEmployeeId = this.assignedToEmployeeId,
    createdByEmployeeId = this.createdByEmployeeId,
    status = this.status.name,
    createdAt = this.createdAt
)

fun Task.asTaskAssignedEvent() = TaskAssigned(
    eventProducer = "task-service",
    taskId = this.taskId!!,
    assignedToEmployeeId = this.assignedToEmployeeId!!
)

fun Task.asTaskCompletedEvent() = TaskCompleted(
    eventProducer = "task-service",
    taskId = this.taskId!!
)

fun Task.asTaskClosedEvent() = TaskClosed(
    eventProducer = "task-service",
    taskId = this.taskId!!
)
