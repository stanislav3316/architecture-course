package com.uberpopug.app.listener

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.uberpopug.app.data.RawEventRepository
import com.uberpopug.app.data.employee.Employee
import com.uberpopug.app.data.employee.EmployeeRepository
import com.uberpopug.schema.AppEvent
import com.uberpopug.schema.EmployeeCreated
import com.uberpopug.schema.EmployeeRoleChanged
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class EmployeeEventListener(
    private val objectMapper: ObjectMapper,
    private val employeeRepository: EmployeeRepository,
    private val rawEventRepository: RawEventRepository
) {
    private val log = LoggerFactory.getLogger(EmployeeEventListener::class.java)

    @KafkaListener(
        topics = ["employee", "employee-stream"],
        groupId = "analytics"
    )
    fun onDomainEvent(@Payload event: String, acknowledgment: Acknowledgment) {

        try {

            val employeeEvent = objectMapper.readValue<AppEvent>(event)
            rawEventRepository.save(employeeEvent)

            when (employeeEvent) {

                is EmployeeCreated -> {
                    val employee = Employee(
                        employeeId = employeeEvent.employeeId,
                        firstName = employeeEvent.firstName,
                        lastName = employeeEvent.lastName,
                        phoneNumber = employeeEvent.phoneNumber,
                        email = employeeEvent.email,
                        slack = employeeEvent.slack,
                        role = employeeEvent.role,
                        createdAt = employeeEvent.createdAt
                    )
                    employeeRepository.save(employee)
                }

                is EmployeeRoleChanged -> {
                    employeeRepository.updateRole(
                        employeeId = employeeEvent.employeeId,
                        role = employeeEvent.newRole
                    )
                }

                else -> log.debug("event - $employeeEvent was ignored")
            }

        } catch (e: Exception) {
            log.error("cannot deserialize employee event - {}", event)
            //todo: publish to dead letters topic
        }

        acknowledgment.acknowledge()
    }
}
