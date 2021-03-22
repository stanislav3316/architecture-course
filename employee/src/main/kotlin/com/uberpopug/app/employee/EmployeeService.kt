package com.uberpopug.app.employee

import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import javax.annotation.PostConstruct
import kotlin.random.Random

@Service
class EmployeeService(val employeeRepository: EmployeeRepository) {

    @PostConstruct
    fun init() {
        //todo: tmp -> for test OAuth only
        employeeRepository.save(
            Employee(
                employeeId = null,
                firstName = "aaaa",
                lastName = "bbbb",
                phoneNumber = "+79045578397",
                createdAt = OffsetDateTime.now(),
                version = 0
            )
        )
    }

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
