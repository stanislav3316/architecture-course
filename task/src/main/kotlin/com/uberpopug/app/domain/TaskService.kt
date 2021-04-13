package com.uberpopug.app.domain

import com.uberpopug.app.asTaskAssignedEvent
import com.uberpopug.app.asTaskClosedEvent
import com.uberpopug.app.asTaskCompletedEvent
import com.uberpopug.app.asTaskCreatedEvent
import com.uberpopug.app.streaming.employee.EmployeeRepository
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val employeeRepository: EmployeeRepository,
    private val kafkaTemplate: KafkaTemplate<Any, Any>
) {
    private val businessTopic = "task"
    private val streamTopic = "task-stream"

    fun create(command: CreateNewTaskCommand): Task {
        val task = Task.create(command)
        return taskRepository.save(task).apply {
            val event = this.asTaskCreatedEvent()
            kafkaTemplate.send(businessTopic, this.taskId!!, event)
            kafkaTemplate.send(streamTopic, this.taskId!!, event)
        }
    }

    fun show(taskId: String): Task {
        return taskRepository.findById(taskId).orElseGet {
            throw TaskNotFound(taskId)
        }
    }

    fun assignTasks() {
        val tasksForAssign =
            taskRepository.findAllByStatus(TaskStatus.NEW) +
            taskRepository.findAllByStatus(TaskStatus.IN_PROGRESS)

        tasksForAssign.forEach { task ->
            val employee = employeeRepository.findRandomOne()
            val assignedTask = task.assignEmployee(employee.employeeId)

            taskRepository.save(assignedTask).apply {
                val event = this.asTaskAssignedEvent()
                kafkaTemplate.send(businessTopic, this.taskId!!, event)
                kafkaTemplate.send(streamTopic, this.taskId!!, event)
            }
        }
    }

    fun complete(taskId: String) {
        val task = taskRepository.findById(taskId).orElseGet {
            throw TaskNotFound(taskId)
        }

        val completedTask = task.complete()
        taskRepository.save(completedTask).apply {
            val event = this.asTaskCompletedEvent()
            kafkaTemplate.send(businessTopic, this.taskId!!, event)
            kafkaTemplate.send(streamTopic, this.taskId!!, event)
        }
    }

    fun close(taskId: String) {
        val task = taskRepository.findById(taskId).orElseGet {
            throw TaskNotFound(taskId)
        }

        val closedTask = task.close()
        taskRepository.save(closedTask).apply {
            kafkaTemplate.send(streamTopic, this.taskId!!, this.asTaskClosedEvent())
        }
    }

    fun getTaskAssignedForEmployeeId(employeeId: String): List<Task> {
        return taskRepository.findAllByAssignedToEmployeeId(employeeId)
    }
}