package com.uberpopug.accounting.domain

import com.uberpopug.accounting.asAccountOpenedEvent
import com.uberpopug.accounting.domain.transaction.Transaction
import com.uberpopug.accounting.domain.transaction.TransactionRepository
import com.uberpopug.accounting.streaming.task.Task
import com.uberpopug.schema.EmployeeDayClosed
import com.uberpopug.schema.PayedForAssignedTask
import com.uberpopug.schema.PayedForCompletedTask
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class AccountingService(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val kafkaTemplate: KafkaTemplate<Any, Any>
) {
    private val businessTopic = "accounting"
    private val streamTopic = "accounting-stream"

    @Transactional
    fun openNewAccount(employeeId: String) {
        val account = Account.new(employeeId)
        accountRepository.save(account).apply {
            kafkaTemplate.send(streamTopic, this.accountId!!, this.asAccountOpenedEvent())
        }
    }

    @Transactional
    fun payForCompletedTask(employeeId: String, task: Task) {

        val employeeAccount = accountRepository.findAccountByEmployeeId(employeeId).orElseThrow {
            throw IllegalStateException("employee account wasn't opened")
        }

        val completedValue = task.completedTaskValue

        val transaction = Transaction.taskCompleted(
            completedTaskValue = completedValue,
            employeeAccountId = employeeAccount.accountId!!,
            taskId = task.taskId
        )
        transactionRepository.save(transaction)

        val refilledAccount = employeeAccount.refill(completedValue)
        accountRepository.save(refilledAccount)

        val event = PayedForCompletedTask("accounting", employeeAccount.accountId!!, completedValue)
        kafkaTemplate.send(streamTopic, employeeAccount.accountId!!, event)
    }

    @Transactional
    fun payForAssignedTask(employeeId: String, task: Task) {

        val employeeAccount = accountRepository.findAccountByEmployeeId(employeeId).orElseThrow {
            throw IllegalStateException("employee account wasn't opened")
        }

        val assignValue = task.assignedTaskValue

        val transaction = Transaction.taskAssigned(
            assignedTaskValue = assignValue,
            employeeAccountId = employeeAccount.accountId!!,
            taskId = task.taskId
        )
        transactionRepository.save(transaction)

        val withdrawnAccount = employeeAccount.withdraw(assignValue)
        accountRepository.save(withdrawnAccount)

        val event = PayedForAssignedTask("accounting", employeeAccount.accountId!!, assignValue)
        kafkaTemplate.send(streamTopic, employeeAccount.accountId!!, event)
    }

    @Transactional
    fun closeEmployeesDay() {
        //todo: тут все плохо (длинная транзакция, возможны перебои на границе дня и т д еще и пуш в кафку в транзакции)
        accountRepository.findAll().forEach { account ->
            if (account.amount > BigDecimal.ZERO) {

                val transaction = Transaction.closedDay(
                    amount = account.amount,
                    employeeAccountId = account.accountId!!
                )
                transactionRepository.save(transaction)

                val withdrawnAccount = account.fullWithdraw()
                accountRepository.save(withdrawnAccount)

                val event = EmployeeDayClosed("accounting", account.employeeId, account.accountId!!, account.amount)
                kafkaTemplate.send(businessTopic, account.accountId!!, event)
                kafkaTemplate.send(streamTopic, account.accountId!!, event)
            }
        }
    }
}
