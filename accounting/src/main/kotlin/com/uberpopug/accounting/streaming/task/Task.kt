package com.uberpopug.accounting.streaming.task

import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Task(
    @Id
    val taskId: String,
    val price: BigDecimal,
    val assignedToEmployeeId: String,
    val status: String
)
