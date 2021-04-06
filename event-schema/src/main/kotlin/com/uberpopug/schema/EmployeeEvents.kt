package com.uberpopug.schema

import java.time.OffsetDateTime

class EmployeeCreated(
    eventProducer: String,
    val employeeId: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val slack: String,
    val email: String,
    val role: String,
    val createdAt: OffsetDateTime
) : AppEvent(
    meta = EventMetaData("employee-created", 1, eventProducer)
)

class EmployeeRoleChanged(
    eventProducer: String,
    val employeeId: String,
    val newRole: String
) : AppEvent(
    meta = EventMetaData("employee-role-changed", 1, eventProducer)
)

class EmployeeAuthenticated(
    eventProducer: String,
    val employeeId: String
) : AppEvent(
    meta = EventMetaData("employee-authenticated", 1, eventProducer)
)
