package com.uberpopug.task.notification

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class NotificationService {

    private val log = LoggerFactory.getLogger(NotificationService::class.java)

    fun notifyWithAssignedTask(employeeId: String, taskId: String) {
        log.info("employee was notified by slack, email and sms")
    }
}
