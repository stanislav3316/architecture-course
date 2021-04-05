package com.uberpopug.accounting.streaming.task

import com.uberpopug.accounting.asTaskPriceEstimatedEvent
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.math.BigDecimal
import kotlin.random.Random

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val kafkaTemplate: KafkaTemplate<Any, Any>
) {
    private val streamTopic = "accounting-streaming"

    fun estimateAndSave(taskId: String, assignedToEmployeeId: String?, status: String) {
        val task = Task(
            taskId = taskId,
            completedTaskValue = estimateValue(smallestValue = 20, biggestValue = 40),
            assignedTaskValue = estimateValue(smallestValue = 10, biggestValue = 20),
            assignedToEmployeeId = assignedToEmployeeId,
            status = status,
            version = 0
        )

        taskRepository.save(task).apply {
            kafkaTemplate.send(streamTopic, this.asTaskPriceEstimatedEvent(), this.taskId)
        }
    }

    fun markTaskAsAssigned(taskId: String, assignedToEmployeeId: String) {
        val task = find(taskId)
        val assignedTask = task.asAssigned(assignedToEmployeeId)
        taskRepository.save(assignedTask)
    }

    fun markAsCompleted(taskId: String) {
        val task = find(taskId)
        val assignedTask = task.asCompleted()
        taskRepository.save(assignedTask)
    }

    fun find(taskId: String): Task {
        return taskRepository.findById(taskId).orElseThrow {
            throw IllegalStateException("task not found")
        }
    }

    private fun estimateValue(smallestValue: Int, biggestValue: Int) =
        Random.nextInt(from = smallestValue, until = biggestValue).toBigDecimal()
}
