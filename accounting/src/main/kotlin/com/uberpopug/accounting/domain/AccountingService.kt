package com.uberpopug.accounting.domain

import com.uberpopug.accounting.domain.transaction.TransactionService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountingService(
    private val transactionService: TransactionService,
    private val accountRepository: AccountRepository
) {

    @Transactional
    fun createAccount() {

    }

    @Transactional
    fun payForCompletedTask() {

    }

    @Transactional
    fun payForAssignedTask() {

    }

    @Transactional
    fun closeEmployeesDay() {

    }
}
