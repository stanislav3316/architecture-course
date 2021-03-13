package com.uberpopug.app.domain.notification

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class NotificationService {

    private val log = LoggerFactory.getLogger(NotificationService::class.java)

    //todo: should be TaskAssigned event
    fun notifyAssignedEmployee(taskId: String, employeeId: String) {
        log.info("employee - $employeeId was assigned with task - $taskId")
    }
}
