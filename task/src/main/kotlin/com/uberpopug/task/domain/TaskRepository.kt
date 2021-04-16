package com.uberpopug.task.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : JpaRepository<Task, String> {

    fun findAllByAssignedToEmployeeId(employeeId: String): List<Task>
    fun findAllByStatus(status: TaskStatus): List<Task>
}
