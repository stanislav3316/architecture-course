package com.uberpopug.accounting.listener.task.handler

import com.uberpopug.schema.AppEvent
import com.uberpopug.schema.AppEventHandler
import com.uberpopug.schema.TaskCompleted
import org.springframework.stereotype.Service

@Service
class TaskCompletedHandler : AppEventHandler {

    override fun isSuitableFor(event: AppEvent): Boolean {
        return event is TaskCompleted && event.meta.eventVersion == 1
    }

    override fun handle(event: AppEvent) {
        event as TaskCompleted
        //todo: handle TaskCompleted event -> pay for completed task
    }
}
