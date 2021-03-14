package com.uberpopug.app.domain.task

interface TaskCommand

data class CreateNewTaskCommand(
    val title: String,
    val description: String,
    val createdByEmployeeId: String
) : TaskCommand
