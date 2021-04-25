package com.uberpopug.employee.publisher

import com.fasterxml.jackson.databind.ObjectMapper
import com.uberpopug.employee.asEmployeeAuthenticatedEvent
import com.uberpopug.employee.asEmployeeCreatedEvent
import com.uberpopug.employee.asEmployeeRoleChangedEvent
import com.uberpopug.employee.domain.Employee
import com.uberpopug.schema.AppEvent
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class EmployeePublisher(
    private val kafkaTemplate: KafkaTemplate<Any, Any>,
    private val mapper: ObjectMapper
) {
    private val businessTopic = "employee"
    private val streamTopic = "employee-stream"

    fun onCreate(employee: Employee) {
        val employeeId = employee.employeeId!!
        val serializedEvent  = serializeEvent(employee.asEmployeeCreatedEvent())
        kafkaTemplate.send(businessTopic, employeeId, serializedEvent)
        kafkaTemplate.send(streamTopic, employeeId, serializedEvent)
    }

    fun onRoleChanged(employee: Employee) {
        val employeeId = employee.employeeId!!
        val serializedEvent  = serializeEvent(employee.asEmployeeRoleChangedEvent())
        kafkaTemplate.send(streamTopic, employeeId, serializedEvent)
    }

    fun onShowForAuthentication(employee: Employee) {
        val employeeId = employee.employeeId!!
        val serializedEvent  = serializeEvent(employee.asEmployeeAuthenticatedEvent())
        kafkaTemplate.send(streamTopic, employeeId, serializedEvent)
    }

    private fun serializeEvent(event: AppEvent) = mapper.writeValueAsString(event)
}
