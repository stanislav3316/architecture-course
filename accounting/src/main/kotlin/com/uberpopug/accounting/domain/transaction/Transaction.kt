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
    val transactionId: String?,
    val amount: BigDecimal,
    @Enumerated(EnumType.STRING)
    val type: TransactionType,
    val toAccount: String,
    val fromAccount: String,
    val taskId: String?,
    val createdAt: OffsetDateTime
) {
    companion object {

        private const val systemAccountId = "-1"

        fun taskAssigned(
            assignedTaskValue: BigDecimal,
            employeeAccountId: String,
            taskId: String
        ) = Transaction(
            transactionId = null,
            amount = assignedTaskValue,
            type = TransactionType.PAY_FOR_ASSIGNED_TASK,
            toAccount = systemAccountId,
            fromAccount = employeeAccountId,
            taskId = taskId,
            createdAt = OffsetDateTime.now()
        )

        fun taskCompleted(
            completedTaskValue: BigDecimal,
            employeeAccountId: String,
            taskId: String
        ) = Transaction(
            transactionId = null,
            amount = completedTaskValue,
            type = TransactionType.PAY_FOR_COMPLETED_TASK,
            toAccount = employeeAccountId,
            fromAccount = systemAccountId,
            taskId = taskId,
            createdAt = OffsetDateTime.now()
        )

        fun closedDay(
            amount: BigDecimal,
            employeeAccountId: String,
        ) = Transaction(
            transactionId = null,
            amount = amount,
            type = TransactionType.CLOSED_EMPLOYEE_DAY,
            toAccount = systemAccountId,
            fromAccount = employeeAccountId,
            taskId = null,
            createdAt = OffsetDateTime.now()
        )
    }
}

enum class TransactionType {
    PAY_FOR_ASSIGNED_TASK,
    PAY_FOR_COMPLETED_TASK,
    CLOSED_EMPLOYEE_DAY
}
