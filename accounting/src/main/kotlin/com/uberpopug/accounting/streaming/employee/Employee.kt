package com.uberpopug.accounting.streaming.employee

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Version

@Entity
data class Employee(
    @Id
    val employeeId: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val role: String,
    @Version
    val version: Int
)
