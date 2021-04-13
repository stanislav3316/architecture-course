package com.uberpopug.app.data.accounting

import java.math.BigDecimal
import java.time.OffsetDateTime

data class Transaction(
    val transactionId: String?,
    val amount: BigDecimal,
    val type: String,
    val toAccount: String,
    val fromAccount: String,
    val taskId: String?,
    val createdAt: OffsetDateTime
)
