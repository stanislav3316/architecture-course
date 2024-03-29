package com.uberpopug.task.controller

import com.uberpopug.task.domain.CreateNewTaskCommand
import com.uberpopug.task.domain.Task
import com.uberpopug.task.domain.TaskService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.annotation.security.RolesAllowed

@RestController
class TaskController(val taskService: TaskService) {

    @RolesAllowed("ADMIN")
    @PostMapping("/v1/task")
    @ResponseStatus(value = HttpStatus.CREATED)
    fun createTask(@RequestBody command: CreateNewTaskCommand): Task {
        return taskService.create(command)
    }

    @RolesAllowed("USER", "ADMIN")
    @GetMapping("/v1/task/{taskId}")
    fun showTask(@PathVariable taskId: String): Task {
        return taskService.show(taskId)
    }

    @RolesAllowed("ADMIN")
    @PostMapping("/v1/task/assign")
    fun assignTasks() {
        taskService.assignTasks()
    }

    @RolesAllowed("USER", "ADMIN")
    @PostMapping("/v1/task/{taskId}/complete")
    fun completeTask(@PathVariable taskId: String) {
        taskService.complete(taskId)
    }

    @RolesAllowed("ADMIN")
    @PostMapping("/v1/task/{taskId}/close")
    fun closeTask(@PathVariable taskId: String) {
        taskService.close(taskId)
    }

    @RolesAllowed("USER", "ADMIN")
    @GetMapping("/v1/task/assignedFor/{employeeId}")
    fun getTaskIdAssignedForEmployeeId(@PathVariable employeeId: String): List<String> {
        return taskService.getTaskAssignedForEmployeeId(employeeId).map { it.taskId!! }
    }
}
