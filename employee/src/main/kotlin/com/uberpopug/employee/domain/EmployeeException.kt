package com.uberpopug.employee.domain

abstract class EmployeeException(val msg: String, val errorCode: String) : RuntimeException(msg)

class InvalidEmployeeParameters : EmployeeException("invalid employee parameters", "invalidEmployeeParameters")

class EmployeeNotFound(uniqueIdentifier: String) : EmployeeException(
    "employee - $uniqueIdentifier not found", "employeeNotFound"
)

class NoAvailableEmployee : EmployeeException("no available employee", "noAvailableEmployee")

class EmployeeHasTheSameRole(
    employeeId: String,
    role: EmployeeRole
) : EmployeeException("employee - $employeeId has the same role - $role", "employeeHasTheSameRole")
