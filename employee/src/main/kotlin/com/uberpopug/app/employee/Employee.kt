package com.uberpopug.app.employee

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.OffsetDateTime
import java.time.OffsetDateTime.now
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
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
    @Enumerated(EnumType.STRING)
    val role: EmployeeRole,
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
                role = command.role,
                phoneNumber = command.phoneNumber,
                createdAt = now(),
                version = 0
            )
        }
    }

    fun changeRole(newRole: EmployeeRole): Employee {
        if (newRole == role) {
            throw EmployeeHasTheSameRole(employeeId!!, newRole)
        }

        return copy(role = newRole)
    }
}

enum class EmployeeRole {
    EMPLOYEE,
    ADMIN,
    MANAGER
}
