package com.uberpopug.task.listener

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.uberpopug.task.streaming.employee.Employee
import com.uberpopug.task.streaming.employee.EmployeeRepository
import com.uberpopug.schema.AppEvent
import com.uberpopug.schema.EmployeeCreated
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class EmployeeEventListener(
    private val objectMapper: ObjectMapper,
    private val employeeRepository: EmployeeRepository
) {
    private val log = LoggerFactory.getLogger(EmployeeEventListener::class.java)

    @KafkaListener(
        topics = ["employee"],
        groupId = "task-service"
    )
    fun onDomainEvent(@Payload event: String, acknowledgment: Acknowledgment) {

        try {
            when (val employeeEvent = objectMapper.readValue<AppEvent>(event)) {
                is EmployeeCreated -> {
                    val employee = employeeRepository.save(
                        Employee(
                            employeeId = employeeEvent.employeeId,
                            firstName = employeeEvent.firstName,
                            lastName = employeeEvent.lastName,
                            phoneNumber = employeeEvent.phoneNumber,
                            email = employeeEvent.email,
                            slack = employeeEvent.slack,
                            role = employeeEvent.role
                        )
                    )
                    log.info("employee - $employee was saved")
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
