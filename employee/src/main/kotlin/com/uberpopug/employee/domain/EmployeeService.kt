package com.uberpopug.employee.domain

import com.uberpopug.employee.config.SecurityConfig
import com.uberpopug.employee.publisher.EmployeePublisher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class EmployeeService(
    private val employeeRepository: EmployeeRepository,
    private val employeePublisher: EmployeePublisher
) {
    private val passwordEncoder: PasswordEncoder = SecurityConfig.encoder

    fun create(command: CreateEmployee): Employee {
        val commandWithHashedPassword = command.copy(password = passwordEncoder.encode(command.password))
        val employee = Employee.create(commandWithHashedPassword)
        return employeeRepository.save(employee).apply {
            employeePublisher.onCreate(this)
        }
    }

    fun changeRole(command: ChangeEmployeeRole) {
        val employeeId = command.employeeId
        val employee = employeeRepository.findById(employeeId).orElseGet {
            throw EmployeeNotFound(employeeId)
        }

        val employeeWithChangedRole = employee.changeRole(command.newRole)
        employeeRepository.save(employeeWithChangedRole).apply {
            employeePublisher.onRoleChanged(this)
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

        employeePublisher.onShowForAuthentication(employee)

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
