package com.uberpopug.accounting.streaming.task

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : CrudRepository<Task, String>
