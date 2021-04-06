package com.uberpopug.accounting.listener.employee.handler

import com.uberpopug.accounting.domain.AccountingService
import com.uberpopug.accounting.streaming.employee.Employee
import com.uberpopug.accounting.streaming.employee.EmployeeRepository
import com.uberpopug.schema.AppEvent
import com.uberpopug.schema.AppEventHandler
import com.uberpopug.schema.EmployeeCreated
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate

@Service
class EmployeeCreatedHandler(
    private val accountingService: AccountingService,
    private val employeeRepository: EmployeeRepository,
    private val transactionTemplate: TransactionTemplate
) : AppEventHandler {

    override fun isSuitableFor(event: AppEvent) = event is EmployeeCreated && event.meta.eventVersion == 1

    override fun handle(event: AppEvent) {
        event as EmployeeCreated

        //todo: норм ли так создавать агрегат? или тут норм анемичненько (думаю что вызов репозитория норм)
        val employee = Employee(
            employeeId = event.employeeId,
            firstName = event.firstName,
            lastName = event.lastName,
            phoneNumber = event.phoneNumber,
            email = event.email,
            slack = event.slack,
            role = event.role
        )

        transactionTemplate.execute {
            employeeRepository.save(employee)
            accountingService.openNewAccount(employee.employeeId)
        }
    }
}
