package com.uberpopug.accounting.listener.task.handler

import com.uberpopug.schema.AppEvent
import com.uberpopug.schema.AppEventHandler
import com.uberpopug.schema.TaskCreated
import org.springframework.stereotype.Service

@Service
class TaskCreatedHandler : AppEventHandler {

    override fun isSuitableFor(event: AppEvent): Boolean {
        return event is TaskCreated && event.meta.eventVersion == 1
    }

    override fun handle(event: AppEvent) {
        event as TaskCreated
        //todo: handle TaskCreated event -> estimate task
    }
}
