package com.uberpopug.accounting

import com.uberpopug.accounting.domain.Account
import com.uberpopug.accounting.streaming.task.Task
import com.uberpopug.schema.AccountOpened
import com.uberpopug.schema.TaskPriceEstimated

fun Account.asAccountOpenedEvent() = AccountOpened(
    eventProducer = "accounting",
    accountId = this.accountId!!,
    employeeId = this.employeeId,
    accountCreatedAt = this.createdAt
)

fun Task.asTaskPriceEstimatedEvent() = TaskPriceEstimated(
    eventProducer = "accounting",
    taskId = this.taskId,
    completedTaskValue = this.completedTaskValue,
    assignedTaskValue = this.assignedTaskValue
)

//todo: add event inside for debug
