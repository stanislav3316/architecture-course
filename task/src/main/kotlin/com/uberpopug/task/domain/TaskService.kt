package com.uberpopug.task.domain

import com.uberpopug.task.publisher.TaskPublisher
import com.uberpopug.task.streaming.employee.EmployeeRepository
import org.springframework.stereotype.Service

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val employeeRepository: EmployeeRepository,
    private val taskPublisher: TaskPublisher
) {

    fun create(command: CreateNewTaskCommand): Task {
        val task = Task.create(command)
        return taskRepository.save(task).apply {
            taskPublisher.onCreate(this)
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
            val employee = employeeRepository.findAll().shuffled().first()
            val assignedTask = task.assignEmployee(employee.employeeId)

            taskRepository.save(assignedTask).apply {
                taskPublisher.onAssign(this)
            }
        }
    }

    fun complete(taskId: String) {
        val task = taskRepository.findById(taskId).orElseGet {
            throw TaskNotFound(taskId)
        }

        val completedTask = task.complete()
        taskRepository.save(completedTask).apply {
            taskPublisher.onComplete(this)
        }
    }

    fun close(taskId: String) {
        val task = taskRepository.findById(taskId).orElseGet {
            throw TaskNotFound(taskId)
        }

        val closedTask = task.close()
        taskRepository.save(closedTask).apply {
            taskPublisher.onClose(this)
        }
    }

    fun getTaskAssignedForEmployeeId(employeeId: String): List<Task> {
        return taskRepository.findAllByAssignedToEmployeeId(employeeId)
    }
}
