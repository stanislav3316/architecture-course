package com.uberpopug.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PopugJiraApplication

fun main(args: Array<String>) {
    runApplication<PopugJiraApplication>(*args)
}
