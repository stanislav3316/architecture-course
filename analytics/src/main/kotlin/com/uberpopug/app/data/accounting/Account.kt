package com.uberpopug.app.data.accounting

import java.time.LocalDate
import java.time.OffsetDateTime

data class Account(
    val accountId: String?,
    val employeeId: String,
    val lastClosedDay: LocalDate?,
    val createdAt: OffsetDateTime
)
