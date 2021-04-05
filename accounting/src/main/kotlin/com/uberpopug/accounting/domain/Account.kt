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
    val accountId: String?,
    val employeeId: String,
    val amount: BigDecimal,
    @Enumerated(EnumType.STRING)
    val status: AccountStatus,
    val lastClosedDay: LocalDate?,
    val lastUpdateAt: OffsetDateTime,
    val createdAt: OffsetDateTime,
    @Version
    @JsonIgnore
    val version: Int
) {
    companion object {
        fun new(employeeId: String): Account {
            val now = OffsetDateTime.now()
            return Account(
                accountId = null,
                employeeId = employeeId,
                amount = BigDecimal.ZERO,
                status = AccountStatus.ACTIVE,
                lastClosedDay = null,
                lastUpdateAt = now,
                createdAt = now,
                version = 0
            )
        }
    }

    fun withdraw(money: BigDecimal): Account {
        return this.copy(
            amount = amount - money
        )
    }

    fun fullWithdraw(): Account = this.copy(amount = BigDecimal.ZERO)

    fun refill(money: BigDecimal): Account {
        return this.copy(
            amount = amount + money
        )
    }
}

enum class AccountStatus {
    ACTIVE,
    CLOSED
}
