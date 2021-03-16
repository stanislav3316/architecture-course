package com.uberpopug.app.employee

abstract class EmployeeException(val msg: String, val errorCode: String) : RuntimeException(msg)

class InvalidEmployeeParameters : EmployeeException("invalid employee parameters", "invalidEmployeeParameters")

class EmployeeNotFound(employeeId: String) : EmployeeException("employee - $employeeId not found", "employeeNotFound")

class NoAvailableEmployee : EmployeeException("no available employee", "noAvailableEmployee")
