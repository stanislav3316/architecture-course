package com.uberpopug.accounting.listener.task.handler

import com.uberpopug.accounting.domain.AccountingService
import com.uberpopug.accounting.streaming.task.TaskService
import com.uberpopug.schema.AppEvent
import com.uberpopug.schema.AppEventHandler
import com.uberpopug.schema.TaskCompleted
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate

@Service
class TaskCompletedHandler(
    private val accountingService: AccountingService,
    private val taskService: TaskService,
    private val transactionTemplate: TransactionTemplate
) : AppEventHandler {

    override fun isSuitableFor(event: AppEvent) = event is TaskCompleted && event.meta.eventVersion == 1

    override fun handle(event: AppEvent) {
        event as TaskCompleted

        transactionTemplate.execute {
            val taskId = event.taskId
            val task = taskService.find(taskId)
            taskService.markAsCompleted(taskId)
            accountingService.payForCompletedTask(task.assignedToEmployeeId!!, task)
        }
    }
}
