package com.uberpopug.app

import com.uberpopug.app.employee.Employee
import com.uberpopug.schema.EmployeeAuthenticated
import com.uberpopug.schema.EmployeeCreated
import com.uberpopug.schema.EmployeeRoleChanged

fun Employee.asEmployeeCreatedEvent() = EmployeeCreated(
    eventProducer = "employee-service",
    employeeId = this.employeeId!!,
    firstName = this.firstName,
    lastName = this.lastName,
    phoneNumber = this.phoneNumber,
    email = this.email,
    slack = this.slack,
    role = this.role.name,
    createdAt = this.createdAt
)

fun Employee.asEmployeeRoleChangedEvent() = EmployeeRoleChanged(
    eventProducer = "employee-service",
    employeeId = this.employeeId!!,
    newRole = this.role.name
)

fun Employee.asEmployeeAuthenticatedEvent() = EmployeeAuthenticated(
    eventProducer = "employee-service",
    employeeId = this.employeeId!!
)
