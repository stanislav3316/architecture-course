package com.uberpopug.accounting.listener.employee.handler

import com.uberpopug.accounting.domain.AccountingService
import com.uberpopug.schema.AppEvent
import com.uberpopug.schema.AppEventHandler
import com.uberpopug.schema.EmployeeCreated
import org.springframework.stereotype.Service

@Service
class EmployeeCreatedHandler(private val accountingService: AccountingService) : AppEventHandler {

    override fun isSuitableFor(event: AppEvent): Boolean {
        return event is EmployeeCreated && event.meta.eventVersion == 1
    }

    override fun handle(event: AppEvent) {
        event as EmployeeCreated
        //todo: handle EmployeeCreated event -> create account with wallet
    }
}
