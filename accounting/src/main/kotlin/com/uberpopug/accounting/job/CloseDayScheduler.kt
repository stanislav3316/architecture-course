package com.uberpopug.accounting.job

import com.uberpopug.accounting.domain.AccountingService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CloseDayScheduler(private val accountingService: AccountingService) {

    @Scheduled(cron = "0 0 0 * * *")
    fun schedule() {
        accountingService.closeEmployeesDay()
    }
}
