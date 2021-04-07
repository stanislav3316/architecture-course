package com.uberpopug.accounting.listener.task.handler

import com.uberpopug.accounting.streaming.task.TaskService
import com.uberpopug.schema.AppEvent
import com.uberpopug.schema.AppEventHandler
import com.uberpopug.schema.TaskCreated
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate

@Service
class TaskCreatedHandler(
    private val taskService: TaskService,
    private val transactionTemplate: TransactionTemplate
) : AppEventHandler {

    override fun isSuitableFor(event: AppEvent) = event is TaskCreated && event.meta.eventVersion == 1

    override fun handle(event: AppEvent) {
        event as TaskCreated

        transactionTemplate.execute {
            taskService.estimateAndSave(
                taskId = event.taskId,
                assignedToEmployeeId = event.assignedToEmployeeId,
                status = event.status
            )
        }
    }
}
