package com.uberpopug.schema

import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.OffsetDateTime
import java.util.UUID

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, property = "type")
sealed class EmployeeDomainEvent<T>(
    val payload: T,
    val eventName: String,
    val eventVersion: Int,
    val producer: String,
    val eventId: String = UUID.randomUUID().toString(),
    val eventTime: OffsetDateTime = OffsetDateTime.now()
)

class EmployeeCreated(
    payload: EmployeeData,
    producer: String
) : EmployeeDomainEvent<EmployeeData>(payload, "employee-created", 1, producer)

class EmployeeRoleChanged(
    payload: EmployeeData,
    producer: String
) : EmployeeDomainEvent<EmployeeData>(payload, "employee-role-changed", 1, producer)

class EmployeeAuthenticated(
    payload: EmployeeData,
    producer: String
) : EmployeeDomainEvent<EmployeeData>(payload, "employee-authenticated", 1, producer)

// класс для данных о Employee, который шарится в либе и доступен для версии v1 в событиях
data class EmployeeData(
    val employeeId: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val role: String,
    val createdAt: OffsetDateTime
)
