package com.uberpopug.app.domain.task

import com.uberpopug.app.asTaskAssignedEvent
import com.uberpopug.app.asTaskClosedEvent
import com.uberpopug.app.asTaskCompletedEvent
import com.uberpopug.app.asTaskCreatedEvent
import com.uberpopug.app.client.EmployeeClient
import com.uberpopug.app.domain.notification.NotificationService
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val employeeClient: EmployeeClient,
    private val notificationService: NotificationService,
    private val kafkaTemplate: KafkaTemplate<Any, Any>
) {
    private val kafkaAggregateTopic = "task-aggregate"

    fun create(command: CreateNewTaskCommand): Task {
        val task = Task.create(command)
        return taskRepository.save(task).apply {
            kafkaTemplate.send(kafkaAggregateTopic, this.asTaskCreatedEvent())
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
            val employee = employeeClient.findRandomOne()
            val assignedTask = task.assignEmployee(employee.employeeId)

            taskRepository.save(assignedTask).apply {
                kafkaTemplate.send(kafkaAggregateTopic, this.asTaskAssignedEvent())
            }

            notificationService.notifyAssignedEmployee(
                taskId = assignedTask.taskId!!,
                employeeId = assignedTask.assignedToEmployeeId!!
            )
        }
    }

    fun complete(taskId: String) {
        val task = taskRepository.findById(taskId).orElseGet {
            throw TaskNotFound(taskId)
        }

        val completedTask = task.complete()
        taskRepository.save(completedTask).apply {
            kafkaTemplate.send(kafkaAggregateTopic, this.asTaskCompletedEvent())
        }
    }

    fun close(taskId: String) {
        val task = taskRepository.findById(taskId).orElseGet {
            throw TaskNotFound(taskId)
        }

        val closedTask = task.close()
        taskRepository.save(closedTask).apply {
            kafkaTemplate.send(kafkaAggregateTopic, this.asTaskClosedEvent())
        }
    }

    fun getTaskAssignedForEmployeeId(employeeId: String): List<Task> {
        return taskRepository.findAllByAssignedToEmployeeId(employeeId)
    }
}
