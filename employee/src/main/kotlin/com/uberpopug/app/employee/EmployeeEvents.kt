package com.uberpopug.app.employee

import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.OffsetDateTime
import java.util.UUID

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, property = "type")
sealed class EmployeeDomainEvent<T>(
    val payload: T,
    val eventName: String,
    val eventVersion: Int,
    val eventId: String = UUID.randomUUID().toString(),
    val eventTime: OffsetDateTime = OffsetDateTime.now(),
    val producer: String = "task-service"
)

class EmployeeCreated(
    payload: EmployeeData
) : EmployeeDomainEvent<EmployeeData>(payload, "employee-created", 1)

class EmployeeRoleChanged(
    payload: EmployeeData
) : EmployeeDomainEvent<EmployeeData>(payload, "employee-role-changed", 1)

class EmployeeAuthenticated(
    payload: EmployeeData
) : EmployeeDomainEvent<EmployeeData>(payload, "employee-authenticated", 1)

// класс для данных о Employee, который шарится в либе и доступен для версии v1 в событиях
data class EmployeeData(
    val employeeId: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val role: String,
    val createdAt: OffsetDateTime
)
