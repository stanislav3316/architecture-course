package com.uberpopug.app.controller

import com.uberpopug.app.domain.employee.CreateEmployee
import com.uberpopug.app.domain.employee.Employee
import com.uberpopug.app.domain.employee.EmployeeService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class EmployeeController(val employeeService: EmployeeService) {

    @PostMapping("/v1/employee")
    @ResponseStatus(value = HttpStatus.CREATED)
    fun createEmployee(@RequestBody command: CreateEmployee): Employee {
        return employeeService.create(command)
    }

    @GetMapping("/v1/employee/{employeeId}")
    fun getEmployeeById(@PathVariable employeeId: String): Employee {
        return employeeService.getById(employeeId)
    }
}
