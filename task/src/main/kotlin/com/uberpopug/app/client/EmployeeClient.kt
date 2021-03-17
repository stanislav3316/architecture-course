package com.uberpopug.app.client

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class EmployeeClient(private val restTemplate: RestTemplate) {

    private val employeeServiceUrl = "http://localhost:8081"

    //todo: streaming will be, it is tmp solution
    fun findRandomOne(): Employee {
        return restTemplate.getForObject("$employeeServiceUrl/v1/employee/random", Employee::class.java)!!
    }
}

data class Employee(
    val employeeId: String
)
