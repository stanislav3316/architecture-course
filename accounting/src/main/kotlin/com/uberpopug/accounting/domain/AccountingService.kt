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

        val employeeAccount = findByEmployeeId(employeeId)
        val completedTaskValue = task.completedTaskValue

        transactionRepository.save(
            Transaction.taskCompleted(
                completedTaskValue = completedTaskValue,
                employeeAccountId = employeeAccount.accountId!!,
                taskId = task.taskId
            )
        )

        accountRepository.save(
            employeeAccount.refill(completedTaskValue)
        )

        val event = PayedForCompletedTask("accounting", employeeAccount.accountId!!, completedTaskValue)
        kafkaTemplate.send(streamTopic, employeeAccount.accountId!!, event)
    }

    @Transactional
    fun payForAssignedTask(employeeId: String, task: Task) {

        val employeeAccount = findByEmployeeId(employeeId)
        val assignTaskValue = task.assignedTaskValue

        transactionRepository.save(
            Transaction.taskAssigned(
                assignedTaskValue = assignTaskValue,
                employeeAccountId = employeeAccount.accountId!!,
                taskId = task.taskId
            )
        )

        accountRepository.save(
            employeeAccount.withdraw(assignTaskValue)
        )

        val event = PayedForAssignedTask("accounting", employeeAccount.accountId!!, assignTaskValue)
        kafkaTemplate.send(streamTopic, employeeAccount.accountId!!, event)
    }

    @Transactional
    fun closeEmployeesDay() {
        //todo: тут все плохо (длинная транзакция, возможны перебои на границе дня и т д еще и пуш в кафку в транзакции)
        accountRepository.findAll().forEach { account ->
            if (account.amount > BigDecimal.ZERO) {

                transactionRepository.save(
                    Transaction.closedDay(
                        amount = account.amount,
                        employeeAccountId = account.accountId!!
                    )
                )

                accountRepository.save(
                    account.fullWithdraw()
                )

                val event = EmployeeDayClosed("accounting", account.employeeId, account.accountId!!, account.amount)
                kafkaTemplate.send(businessTopic, account.accountId!!, event)
                kafkaTemplate.send(streamTopic, account.accountId!!, event)
            }
        }
    }

    fun findByEmployeeId(employeeId: String): Account {
        return accountRepository.findAccountByEmployeeId(employeeId).orElseThrow {
            throw IllegalStateException("employee account wasn't opened")
        }
    }
}
