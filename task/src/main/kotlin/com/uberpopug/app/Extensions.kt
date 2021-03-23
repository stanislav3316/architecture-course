package com.uberpopug.app

import com.uberpopug.app.domain.task.Task
import com.uberpopug.schema.TaskAssigned
import com.uberpopug.schema.TaskClosed
import com.uberpopug.schema.TaskCompleted
import com.uberpopug.schema.TaskCreated
import com.uberpopug.schema.TaskData

private fun Task.toTaskData() = TaskData(
    taskId = taskId!!,
    title = title,
    description = description,
    assignedToEmployeeId = assignedToEmployeeId,
    createdByEmployeeId = createdByEmployeeId,
    status = status.name,
    createdAt = createdAt
)

fun Task.asTaskCreatedEvent() = TaskCreated(
    payload = this.toTaskData(),
    producer = "task-service"
)

fun Task.asTaskAssignedEvent() = TaskAssigned(
    payload = this.toTaskData(),
    producer = "task-service"
)

fun Task.asTaskCompletedEvent() = TaskCompleted(
    payload = this.toTaskData(),
    producer = "task-service"
)

fun Task.asTaskClosedEvent() = TaskClosed(
    payload = this.toTaskData(),
    producer = "task-service"
)
