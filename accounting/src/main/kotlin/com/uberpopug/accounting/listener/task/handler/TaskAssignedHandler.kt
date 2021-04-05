package com.uberpopug.accounting.listener.task.handler

import com.uberpopug.accounting.domain.AccountingService
import com.uberpopug.accounting.streaming.task.TaskService
import com.uberpopug.schema.AppEvent
import com.uberpopug.schema.AppEventHandler
import com.uberpopug.schema.TaskAssigned
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate

@Service
class TaskAssignedHandler(
    private val accountingService: AccountingService,
    private val taskService: TaskService,
    private val transactionTemplate: TransactionTemplate
) : AppEventHandler {

    override fun isSuitableFor(event: AppEvent) = event is TaskAssigned && event.meta.eventVersion == 1

    override fun handle(event: AppEvent) {
        event as TaskAssigned

        transactionTemplate.execute {
            val task = taskService.find(event.taskId)
            taskService.markTaskAsAssigned(taskId = event.taskId, assignedToEmployeeId = event.assignedToEmployeeId)
            accountingService.payForAssignedTask(task = task, employeeId = event.assignedToEmployeeId)
        }
    }
}
