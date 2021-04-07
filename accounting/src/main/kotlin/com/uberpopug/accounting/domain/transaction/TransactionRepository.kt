package com.uberpopug.accounting.domain.transaction

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : CrudRepository<Transaction, String>
