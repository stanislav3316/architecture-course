package com.uberpopug.accounting.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Version

@Entity
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val employeeId: String,
    val amount: BigDecimal,
    @Enumerated(EnumType.STRING)
    val status: AccountStatus,
    val lastClosedDay: LocalDate,
    val lastUpdateAt: String,
    val createdAt: OffsetDateTime,
    @Version
    @JsonIgnore
    val version: Int
)

enum class AccountStatus {
    ACTIVE,
    CLOSED
}
