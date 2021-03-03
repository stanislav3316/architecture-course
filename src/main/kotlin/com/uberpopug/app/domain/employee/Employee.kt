package com.uberpopug.app.domain.employee

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.OffsetDateTime
import java.time.OffsetDateTime.now
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Version

@Entity
data class Employee(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val employeeId: String?,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val createdAt: OffsetDateTime,
    @Version
    @JsonIgnore
    val version: Int
) {
    companion object {
        // +79092345678
        private val phonePattern = "[+]\\d{11}".toRegex()

        fun create(command: CreateEmployee): Employee {

            if (!phonePattern.matches(command.phoneNumber)) {
                throw InvalidEmployeeParameters()
            }

            return Employee(
                employeeId = null,
                firstName = command.firstName,
                lastName = command.lastName,
                phoneNumber = command.phoneNumber,
                createdAt = now(),
                version = 0
            )
        }
    }
}
