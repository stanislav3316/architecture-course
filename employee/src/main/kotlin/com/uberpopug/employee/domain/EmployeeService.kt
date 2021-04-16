package com.uberpopug.employee.domain

import com.fasterxml.jackson.databind.ObjectMapper
import com.uberpopug.employee.asEmployeeAuthenticatedEvent
import com.uberpopug.employee.asEmployeeCreatedEvent
import com.uberpopug.employee.asEmployeeRoleChangedEvent
import com.uberpopug.employee.config.SecurityConfig
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class EmployeeService(
    private val employeeRepository: EmployeeRepository,
    private val kafkaTemplate: KafkaTemplate<Any, Any>,
    private val objectMapper: ObjectMapper
) {
    private val businessTopic = "employee"
    private val streamTopic = "employee-stream"

    private val passwordEncoder: PasswordEncoder = SecurityConfig.encoder

    fun create(command: CreateEmployee): Employee {
        val commandWithHashedPassword = command.copy(password = passwordEncoder.encode(command.password))
        val employee = Employee.create(commandWithHashedPassword)
        return employeeRepository.save(employee).apply {
            val event = this.asEmployeeCreatedEvent()
            kafkaTemplate.send(businessTopic, this.employeeId!!, event)
            kafkaTemplate.send(streamTopic, this.employeeId!!, event)
        }
    }

    fun changeRole(command: ChangeEmployeeRole) {
        val employeeId = command.employeeId
        val employee = employeeRepository.findById(employeeId).orElseGet {
            throw EmployeeNotFound(employeeId)
        }

        val employeeWithChangedRole = employee.changeRole(command.newRole)
        employeeRepository.save(employeeWithChangedRole).apply {
            kafkaTemplate.send(streamTopic, this.employeeId!!, this.asEmployeeRoleChangedEvent())
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

        kafkaTemplate.send(
            streamTopic,
            employee.employeeId!!,
            objectMapper.writeValueAsString(employee.asEmployeeAuthenticatedEvent())
        )

        //todo: !!!!

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