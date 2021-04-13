package com.uberpopug.app.streaming.employee

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeeRepository : CrudRepository<Employee, String> {

    //todo: tmp
    fun findRandomOne(): Employee = findAll().shuffled().first()
}
