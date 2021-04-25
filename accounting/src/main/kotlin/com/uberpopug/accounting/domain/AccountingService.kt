package com.uberpopug.accounting.domain

import com.uberpopug.accounting.domain.transaction.Transaction
import com.uberpopug.accounting.domain.transaction.TransactionRepository
import com.uberpopug.accounting.publisher.AccountingPublisher
import com.uberpopug.accounting.streaming.task.Task
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class AccountingService(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val accountingPublisher: AccountingPublisher
) {

    @Transactional
    fun openNewAccount(employeeId: String) {
        val account = Account.new(employeeId)
        accountRepository.save(account).apply {
            accountingPublisher.onNewAccount(this)
        }
    }

    @Transactional
    fun payForCompletedTask(employeeId: String, task: Task) {

        val employeeAccount = findByEmployeeId(employeeId)
        val completedTaskValue = task.completedTaskValue

        val transaction = transactionRepository.save(
            Transaction.taskCompleted(
                completedTaskValue = completedTaskValue,
                employeeAccountId = employeeAccount.accountId!!,
                taskId = task.taskId
            )
        )

        accountRepository.save(
            employeeAccount.refill(completedTaskValue)
        )

        accountingPublisher.onPayForCompletedTask(
            account = employeeAccount,
            completedTaskValue = completedTaskValue,
            taskId = task.taskId,
            transactionId = transaction.transactionId!!
        )
    }

    @Transactional
    fun payForAssignedTask(employeeId: String, task: Task) {

        val employeeAccount = findByEmployeeId(employeeId)
        val assignTaskValue = task.assignedTaskValue

        val transaction = transactionRepository.save(
            Transaction.taskAssigned(
                assignedTaskValue = assignTaskValue,
                employeeAccountId = employeeAccount.accountId!!,
                taskId = task.taskId
            )
        )

        accountRepository.save(
            employeeAccount.withdraw(assignTaskValue)
        )

        accountingPublisher.onPayForAssignedTask(
            account = employeeAccount,
            assignTaskValue = assignTaskValue,
            taskId = task.taskId,
            transactionId = transaction.transactionId!!
        )
    }

    @Transactional
    fun closeEmployeesDay() {
        //todo: тут все плохо (длинная транзакция, возможны перебои на границе дня и т д еще и пуш в кафку в транзакции)
        accountRepository.findAll().forEach { account ->
            if (account.amount > BigDecimal.ZERO) {

                val transaction = transactionRepository.save(
                    Transaction.closedDay(
                        amount = account.amount,
                        employeeAccountId = account.accountId!!
                    )
                )

                accountRepository.save(
                    account.fullWithdraw()
                )

                accountingPublisher.onCloseEmployeesDay(account, transaction.transactionId!!)
            }
        }
    }

    fun findByEmployeeId(employeeId: String): Account {
        return accountRepository.findAccountByEmployeeId(employeeId).orElseThrow {
            throw IllegalStateException("employee account wasn't opened")
        }
    }
}
