package com.uberpopug.employee.controller

import com.uberpopug.employee.domain.EmployeeException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class ControllerExceptionHandler {

    private val log = LoggerFactory.getLogger(ControllerExceptionHandler::class.java)

    @ExceptionHandler(value = [EmployeeException::class])
    fun handleEmployeeException(ex: RuntimeException, request: WebRequest): ResponseEntity<Any> {
        ex as EmployeeException
        log.error("${ex.msg} ${ex.errorCode}")
        return ResponseEntity(HttpStatus.BAD_GATEWAY)
    }
}
