package com.uberpopug.app.controller

import com.uberpopug.app.employee.CreateEmployee
import com.uberpopug.app.employee.Employee
import com.uberpopug.app.employee.EmployeeService
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

    @GetMapping("/v1/employee/random")
    fun showRandomEmployee(): Employee {
        return employeeService.findRandomOne()
    }

    @GetMapping("/v1/employee/{employeeId}")
    fun showEmployee(@PathVariable employeeId: String): Employee {
        return employeeService.show(employeeId)
    }
}
