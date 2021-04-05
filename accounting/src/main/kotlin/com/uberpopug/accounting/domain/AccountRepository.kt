package com.uberpopug.accounting.domain

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface AccountRepository : CrudRepository<Account, String> {

    fun findAccountByEmployeeId(employeeId: String): Optional<Account>
}
