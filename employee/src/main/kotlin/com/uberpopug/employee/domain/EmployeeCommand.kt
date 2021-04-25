package com.uberpopug.employee.domain

data class CreateEmployee(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val email: String,
    val slack: String,
    val role: EmployeeRole,
    val password: String
)

data class ChangeEmployeeRole(
    val employeeId: String,
    val newRole: EmployeeRole
)
