package com.uberpopug.app.client

import org.springframework.stereotype.Service

@Service
class EmployeeClient {

    //todo: streaming will be
    fun findRandomOne(): Employee {
        TODO()
    }
}

data class Employee(
    val employeeId: String
)
