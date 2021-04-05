package com.uberpopug.accounting.streaming.task

import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Task(
    @Id
    val taskId: String,
    val completedTaskValue: BigDecimal,
    val assignedTaskValue: BigDecimal,
    val assignedToEmployeeId: String?,
    val status: String
) {
    fun asAssigned(assignedToEmployeeId: String) = this.copy(
        status = "IN_PROGRESS",
        assignedToEmployeeId = assignedToEmployeeId
    )

    fun asCompleted() = this.copy(
        status = "COMPLETED"
    )
}
