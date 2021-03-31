package com.uberpopug.accounting.streaming.task

import org.springframework.stereotype.Service
import java.util.*

@Service
class TaskService(private val taskRepository: TaskRepository) {

    fun save() {

    }

    fun find(taskId: String): Optional<Task> {
        TODO()
    }

    fun calculateTaskValue() {

    }
}
