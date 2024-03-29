package com.uberpopug.task.domain

abstract class TaskException(val msg: String, val errorCode: String) : RuntimeException(msg)

class TaskNotFound(taskId: String) : TaskException("task with id - $taskId not found", "taskNotFound")

class InvalidTaskParameters : TaskException("invalid task parameters", "invalidTaskParameters")

class TaskNotInProgress(taskId: String) : TaskException("task - $taskId not in progress", "taskAlreadyClosed")

class TaskAlreadyClosed(taskId: String) : TaskException("task - $taskId already closed", "taskAlreadyClosed")

class TaskCanNotBeAssigned(taskId: String) : TaskException("task - $taskId cannot be assigned", "taskCanNotBeAssigned")
