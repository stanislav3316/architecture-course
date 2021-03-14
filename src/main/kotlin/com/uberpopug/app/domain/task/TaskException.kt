package com.uberpopug.app.domain.task

abstract class TaskException(val msg: String, val errorCode: String) : RuntimeException(msg)

class TaskNotFound(taskId: String) : TaskException("task with id - $taskId not found", "taskNotFound")

class InvalidTaskParameters : TaskException("invalid task parameters", "invalidTaskParameters")

class TaskNotInProgress(taskId: String) : TaskException("task - $taskId not in progress", "taskAlreadyClosed")

class TaskAlreadyClosed(taskId: String) : TaskException("task - $taskId already closed", "taskAlreadyClosed")

class TaskCanNotBeReopened(taskId: String) : TaskException("task - $taskId cannot be reopened", "taskCanNotBeReopened")

class TaskCanNotBeAssigned(taskId: String) : TaskException("task - $taskId cannot be assigned", "taskCanNotBeAssigned")

class TaskCanNotBeReassigned(taskId: String) : TaskException(
    "task - $taskId cannot be assigned",
    "taskCanNotBeReassigned"
)
