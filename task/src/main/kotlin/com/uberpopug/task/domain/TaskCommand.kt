package com.uberpopug.task.domain

interface TaskCommand

data class CreateNewTaskCommand(
    val title: String,
    val description: String,
    val createdByEmployeeId: String
) : TaskCommand
