package com.uberpopug.app.data.task

import java.math.BigDecimal
import java.time.OffsetDateTime

data class Task(
    val taskId: String,
    val title: String,
    val description: String,
    val assignedToEmployeeId: String?,
    val createdByEmployeeId: String,
    val completedTaskValue: BigDecimal?,
    val assignedTaskValue: BigDecimal?,
    val status: String,
    val createdAt: OffsetDateTime
)
