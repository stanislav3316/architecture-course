package com.uberpopug.schema

import java.math.BigDecimal
import java.time.OffsetDateTime

class AccountOpened(
    eventProducer: String,
    val accountId: String,
    val employeeId: String,
    val accountCreatedAt: OffsetDateTime
) : AppEvent(
    meta = EventMetaData("account-opened", 1, eventProducer)
)

class PayedForAssignedTask(
    eventProducer: String,
    val accountId: String,
    val amount: BigDecimal
) : AppEvent(
    meta = EventMetaData("payed-for-assigned-task", 1, eventProducer)
)

class PayedForCompletedTask(
    eventProducer: String,
    val accountId: String,
    val amount: BigDecimal
) : AppEvent(
    meta = EventMetaData("payed-for-completed-task", 1, eventProducer)
)

class EmployeeDayClosed(
    eventProducer: String,
    val employeeId: String,
    val accountId: String,
    val amount: BigDecimal
) : AppEvent(
    meta = EventMetaData("employee-day-closed", 1, eventProducer)
)

class TaskPriceEstimated(
    eventProducer: String,
    val taskId: String,
    val completedTaskValue: BigDecimal,
    val assignedTaskValue: BigDecimal
) : AppEvent(
    meta = EventMetaData("task-price-estimated", 1, eventProducer)
)

//todo: put event inside event for debugging
