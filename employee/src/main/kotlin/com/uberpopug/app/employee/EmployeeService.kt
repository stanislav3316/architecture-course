package com.uberpopug.app.employee

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class EmployeeService(
    private val employeeRepository: EmployeeRepository,
    private val kafkaTemplate: KafkaTemplate<Any, Any>
) {
    private val kafkaAggregateTopic = "employee-aggregate"

    fun create(command: CreateEmployee): Employee {
        val employee = Employee.create(command)
        return employeeRepository.save(employee).apply {
            kafkaTemplate.send(kafkaAggregateTopic, EmployeeCreated(this.toEventData()))
        }
    }

    fun changeRole(command: ChangeEmployeeRole) {
        val employeeId = command.employeeId
        val employee = employeeRepository.findById(employeeId).orElseGet {
            throw EmployeeNotFound(employeeId)
        }

        val employeeWithChangedRole = employee.changeRole(command.newRole)
        employeeRepository.save(employeeWithChangedRole).apply {
            kafkaTemplate.send(kafkaAggregateTopic, EmployeeRoleChanged(this.toEventData()))
        }
    }

    fun show(employeeId: String): Employee {
        return employeeRepository.findById(employeeId).orElseGet {
            throw EmployeeNotFound(employeeId)
        }
    }

    fun showForAuthentication(phone: String): Employee {
        val employee = employeeRepository.findByPhoneNumber(phone).orElseGet {
            throw EmployeeNotFound(phone)
        }

        kafkaTemplate.send(kafkaAggregateTopic, EmployeeAuthenticated(employee.toEventData()))

        return employee
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

private fun Employee.toEventData() = EmployeeData(
    employeeId = employeeId!!,
    firstName = firstName,
    lastName = lastName,
    phoneNumber = phoneNumber,
    role = role.name,
    createdAt = createdAt
)
