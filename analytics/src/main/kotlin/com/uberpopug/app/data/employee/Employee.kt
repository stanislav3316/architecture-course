package com.uberpopug.app.data.employee

import java.time.OffsetDateTime

data class Employee(
    val employeeId: String?,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val email: String,
    val slack: String,
    val role: String,
    val createdAt: OffsetDateTime
)
