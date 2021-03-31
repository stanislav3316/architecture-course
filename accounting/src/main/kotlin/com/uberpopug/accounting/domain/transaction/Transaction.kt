package com.uberpopug.accounting.domain.transaction

import java.math.BigDecimal
import java.time.OffsetDateTime
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val transactionId: String,
    val amount: BigDecimal,
    @Enumerated(EnumType.STRING)
    val type: TransactionType,
    val toAccount: String,
    val fromAccount: String,
    val createdAt: OffsetDateTime
)

enum class TransactionType {
    PAY_FOR_ASSIGNED_TASK,
    PAY_FOR_COMPLETED_TASK,
    CLOSED_EMPLOYEE_DAY
}
