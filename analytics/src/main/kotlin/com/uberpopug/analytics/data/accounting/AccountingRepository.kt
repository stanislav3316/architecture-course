package com.uberpopug.analytics.data.accounting

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class AccountingRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) {

    fun saveAccount(account: Account) {
        val sql = """
            INSERT INTO account (
                account_id,
                employee_id,
                last_closed_day,
                created_at
            ) VALUES (
                :account_id,
                :employee_id,
                :last_closed_day,
                :created_at
            )
        """.trimIndent()

        val params = MapSqlParameterSource()
            .addValue("account_id", account.accountId)
            .addValue("employee_id", account.employeeId)
            .addValue("last_closed_day", account.lastClosedDay)
            .addValue("created_at", account.createdAt)

        jdbcTemplate.update(sql, params)
    }

    fun saveTransaction(transaction: Transaction) {

        val sql = """
            INSERT INTO transaction (
                transaction_id,
                amount,
                type,
                to_account,
                from_account,
                task_id,
                created_at
            ) VALUES (
                :transaction_id,
                :amount,
                :type,
                :to_account,
                :from_account,
                :task_id,
                :created_at
            )
        """.trimIndent()

        val params = MapSqlParameterSource()
            .addValue("transaction_id", transaction.transactionId)
            .addValue("amount", transaction.amount)
            .addValue("type", transaction.type)
            .addValue("to_account", transaction.toAccount)
            .addValue("from_account", transaction.fromAccount)
            .addValue("task_id", transaction.taskId)
            .addValue("created_at", transaction.createdAt)

        jdbcTemplate.update(sql, params)
    }
}
