package com.uberpopug.app.domain.employee

import com.uberpopug.app.domain.task.TaskException

abstract class EmployeeException(val msg: String, val errorCode: String) : RuntimeException(msg)

class InvalidEmployeeParameters : TaskException("invalid employee parameters", "invalidEmployeeParameters")

class EmployeeNotFound(employeeId: String) : TaskException("employee - $employeeId not found", "employeeNotFound")

class NoAvailableEmployee : TaskException("no available employee", "noAvailableEmployee")
