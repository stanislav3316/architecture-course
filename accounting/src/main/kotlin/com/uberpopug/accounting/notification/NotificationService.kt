package com.uberpopug.accounting.notification

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class NotificationService {

    private val log = LoggerFactory.getLogger(NotificationService::class.java)

    fun notifyWithClosedDay(employeeId: String, accountId: String, amount: BigDecimal) {
        log.info("employee - $employeeId notified with amount - $amount in account - $accountId")
    }
}
