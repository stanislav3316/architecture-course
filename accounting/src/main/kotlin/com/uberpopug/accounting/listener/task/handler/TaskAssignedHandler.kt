package com.uberpopug.accounting.listener.task.handler

import com.uberpopug.schema.AppEvent
import com.uberpopug.schema.AppEventHandler
import com.uberpopug.schema.TaskAssigned
import org.springframework.stereotype.Service

@Service
class TaskAssignedHandler : AppEventHandler {

    override fun isSuitableFor(event: AppEvent): Boolean {
        return event is TaskAssigned && event.meta.eventVersion == 1
    }

    override fun handle(event: AppEvent) {
        event as TaskAssigned
        //todo: handle TaskAssigned event -> withdraw form user's wallet for assigned task
    }
}
