package com.uberpopug.app.domain.task

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
data class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val taskId: String?,
    val title: String,
    val description: String,
    val assignedToEmployeeId: String?,
    val createdByEmployeeId: String,
    @Enumerated(EnumType.STRING)
    val status: TaskStatus,
    val createdAt: OffsetDateTime,
    @Version
    @JsonIgnore
    val version: Int
) {
    companion object {

        private val minDescriptionLength = 20
        private val maxDescriptionLength = 150

        private val minTitleLength = 10
        private val maxTitleLength = 100

        fun create(command: CreateNewTaskCommand): Task {

            val description = command.description
            if (description.length > maxDescriptionLength || description.length < minDescriptionLength) {
                throw InvalidTaskParameters()
            }

            val title = command.title
            if (title.length > maxTitleLength || title.length < minTitleLength) {
                throw InvalidTaskParameters()
            }

            return Task(
                taskId = null,
                title = command.title,
                assignedToEmployeeId = null,
                description = command.description,
                createdByEmployeeId = command.createdByEmployeeId,
                status = TaskStatus.NEW,
                createdAt = now(),
                version = 0
            )
        }
    }

    fun assignEmployee(employeeId: String): Task {

        if (status != TaskStatus.NEW) {
            throw TaskCanNotBeAssigned(taskId!!)
        }

        return copy(
            assignedToEmployeeId = employeeId,
            status = TaskStatus.IN_PROGRESS
        )
    }

    fun complete(): Task {

        if (status != TaskStatus.IN_PROGRESS) {
            throw TaskNotInProgress(taskId!!)
        }

        return copy(status = TaskStatus.COMPLETED)
    }

    fun close(): Task {

        if (status in listOf(TaskStatus.CLOSED, TaskStatus.COMPLETED)) {
            throw TaskAlreadyClosed(taskId!!)
        }

        return copy(status = TaskStatus.CLOSED)
    }
}

enum class TaskStatus {
    NEW,
    IN_PROGRESS,
    COMPLETED,
    CLOSED
}
