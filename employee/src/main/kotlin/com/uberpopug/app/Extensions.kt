package com.uberpopug.app

import com.uberpopug.app.employee.Employee
import com.uberpopug.schema.EmployeeAuthenticated
import com.uberpopug.schema.EmployeeCreated
import com.uberpopug.schema.EmployeeData
import com.uberpopug.schema.EmployeeRoleChanged

private fun Employee.toEmployeeData() = EmployeeData(
    employeeId = employeeId!!,
    firstName = firstName,
    lastName = lastName,
    phoneNumber = phoneNumber,
    role = role.name,
    createdAt = createdAt
)

fun Employee.asEmployeeCreatedEvent() = EmployeeCreated(
    payload = this.toEmployeeData(),
    producer = "employee-service"
)

fun Employee.asEmployeeRoleChangedEvent() = EmployeeRoleChanged(
    payload = this.toEmployeeData(),
    producer = "employee-service"
)

fun Employee.asEmployeeAuthenticatedEvent() = EmployeeAuthenticated(
    payload = this.toEmployeeData(),
    producer = "employee-service"
)
