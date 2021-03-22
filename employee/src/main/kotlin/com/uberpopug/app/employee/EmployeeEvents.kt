package com.uberpopug.app.employee

import java.time.OffsetDateTime
import java.util.UUID

abstract class EmployeeDomainEvent<T>(
    val payload: T,
    val eventName: String,
    val eventId: String = UUID.randomUUID().toString(),
    val eventVersion: Int = 1,
    val eventTime: OffsetDateTime = OffsetDateTime.now(),
    val producer: String = "task-service"
)

class EmployeeCreated(
    payload: Employee
) : EmployeeDomainEvent<Employee>(payload, "employee-created")

class EmployeeRoleChanged(
    payload: Employee
) : EmployeeDomainEvent<Employee>(payload, "employee-role-changed")

class EmployeeAuthenticated(
    payload: Employee
) : EmployeeDomainEvent<Employee>(payload, "employee-authenticated")
