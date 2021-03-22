package com.uberpopug.app.employee

data class CreateEmployee(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val role: EmployeeRole
)

data class ChangeEmployeeRole(
    val employeeId: String,
    val newRole: EmployeeRole
)
