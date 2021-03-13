package com.uberpopug.app.domain.task

import com.uberpopug.app.domain.employee.EmployeeService
import com.uberpopug.app.domain.notification.NotificationService
import org.springframework.stereotype.Service

@Service
class TaskService(
    val taskRepository: TaskRepository,
    val employeeService: EmployeeService,
    val notificationService: NotificationService
) {

    fun create(command: CreateNewTaskCommand): Task {
        val task = Task.create(command)
        return taskRepository.save(task)
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
            val employee = employeeService.findRandomOne()
            val assignedTask = task.assignEmployee(employee.employeeId!!)

            taskRepository.save(assignedTask)
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
        taskRepository.save(completedTask)
    }

    fun close(taskId: String) {
        val task = taskRepository.findById(taskId).orElseGet {
            throw TaskNotFound(taskId)
        }

        val closedTask = task.close()
        taskRepository.save(closedTask)
    }

    fun getTaskAssignedForEmployeeId(employeeId: String): List<Task> {
        return taskRepository.findAllByAssignedToEmployeeId(employeeId)
    }
}
