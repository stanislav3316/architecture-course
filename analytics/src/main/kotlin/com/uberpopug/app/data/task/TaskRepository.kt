package com.uberpopug.app.data.task

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
class TaskRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) {

    fun save(task: Task) {
        val sql = """
            INSERT INTO task (
                task_id,
                title,
                description,
                assigned_to_employee_id,
                created_by_employee_id,
                assigned_task_value,
                completed_task_value,
                status,
                created_at,
                version
            ) VALUES (
                :task_id,
                :title,
                :description,
                :assigned_to_employee_id,
                :created_by_employee_id,
                :assigned_task_value,
                :completed_task_value,
                :status,
                :created_at,
                :version
            )
        """.trimIndent()

        val params = MapSqlParameterSource()
            .addValue("task_id", task.taskId)
            .addValue("title", task.title)
            .addValue("description", task.description)
            .addValue("assigned_to_employee_id", task.assignedToEmployeeId)
            .addValue("created_by_employee_id", task.createdByEmployeeId)
            .addValue("assigned_task_value", task.assignedTaskValue)
            .addValue("completed_task_value", task.completedTaskValue)
            .addValue("status", task.status)
            .addValue("created_at", task.createdAt)

        jdbcTemplate.update(sql, params)
    }

    fun updateAssignedEmployeeId(taskId: String, assignedEmployeeId: String) {

        val sql = """
            UPDATE
                task
            SET 
                assigned_to_employee_id = :assigned_to_employee_id
            WHERE
                task_id = :task_id
        """.trimIndent()

        val params = MapSqlParameterSource()
            .addValue("assigned_to_employee_id", assignedEmployeeId)
            .addValue("task_id", taskId)

        jdbcTemplate.update(sql, params)
    }

    fun updateStatus(taskId: String, status: String) {

        val sql = """
            UPDATE
                task
            SET 
                status = :status
            WHERE
                task_id = :task_id
        """.trimIndent()

        val params = MapSqlParameterSource()
            .addValue("status", status)
            .addValue("task_id", taskId)

        jdbcTemplate.update(sql, params)
    }

    fun updateEstimatedTask(taskId: String, completedTaskValue: BigDecimal, assignedTaskValue: BigDecimal) {

        val sql = """
            UPDATE
                task
            SET 
                assigned_task_value = :assignedTaskValue,
                completed_task_value = :completedTaskValue
            WHERE
                task_id = :task_id
        """.trimIndent()

        val params = MapSqlParameterSource()
            .addValue("assigned_task_value", assignedTaskValue)
            .addValue("completed_task_value", completedTaskValue)
            .addValue("task_id", taskId)

        jdbcTemplate.update(sql, params)
    }
}
