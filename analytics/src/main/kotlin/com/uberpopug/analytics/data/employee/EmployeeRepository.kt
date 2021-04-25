package com.uberpopug.analytics.data.employee

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class EmployeeRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) {

    fun save(employee: Employee) {
        val sql = """
            INSERT INTO employee (
                employee_id,
                first_name,
                last_name,
                phone_number,
                email,
                slack,
                role,
                created_at
            ) VALUES (
                :employee_id,
                :first_name,
                :last_name,
                :phone_number,
                :email,
                :slack,
                :role,
                :created_at
            )
        """.trimIndent()

        val params = MapSqlParameterSource()
            .addValue("employee_id", employee.employeeId)
            .addValue("first_name", employee.firstName)
            .addValue("last_name", employee.lastName)
            .addValue("phone_number", employee.phoneNumber)
            .addValue("email", employee.email)
            .addValue("slack", employee.slack)
            .addValue("role", employee.role)
            .addValue("created_at", employee.createdAt)

        jdbcTemplate.update(sql, params)
    }

    fun updateRole(employeeId: String, role: String) {
        val sql = """
            UPDATE
                employee
            SET
                role = :role
            WHERE
                employee_id = :employee_id
        """.trimIndent()

        val params = MapSqlParameterSource()
            .addValue("role", role)
            .addValue("employee_id", employeeId)

        jdbcTemplate.update(sql, params)
    }
}
