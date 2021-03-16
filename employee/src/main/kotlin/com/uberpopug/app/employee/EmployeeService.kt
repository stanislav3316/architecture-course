package com.uberpopug.app.employee

import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class EmployeeService(val employeeRepository: EmployeeRepository) {

    fun create(command: CreateEmployee): Employee {
        val employee = Employee.create(command)
        return employeeRepository.save(employee)
    }

    fun show(employeeId: String): Employee {
        return employeeRepository.findById(employeeId).orElseGet {
            throw EmployeeNotFound(employeeId)
        }
    }

    fun findRandomOne(): Employee {
        val employeeAmount = employeeRepository.getEmployeeAmount()

        if (employeeAmount.compareTo(0) == 0) {
           throw NoAvailableEmployee()
        }

        val randomEmployeePosition = Random.nextInt(employeeAmount)
        return employeeRepository.findALlWithOffsetAndLimit(
            limit = 1,
            offset = randomEmployeePosition
        ).first()
    }
}
