package com.uberpopug.accounting.streaming.task

import java.lang.IllegalStateException
import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Version

@Entity
data class Task(
    @Id
    val taskId: String,
    val completedTaskValue: BigDecimal,
    val assignedTaskValue: BigDecimal,
    val assignedToEmployeeId: String?,
    val status: String,
    @Version
    val version: Int //todo: а нужны ли тут гарантии?
) {
    fun asAssigned(assignedToEmployeeId: String): Task {

        if (status == "COMPLETED" || status == "CLOSED") {
            throw IllegalStateException("task was finished already")
        }

        return this.copy(
            status = "IN_PROGRESS",
            assignedToEmployeeId = assignedToEmployeeId
        )
    }

    fun asCompleted(): Task {

        if (status == "COMPLETED" || status == "NEW") {
            throw IllegalStateException("task was not started")
        }

        return this.copy(
            status = "COMPLETED"
        )
    }
}
