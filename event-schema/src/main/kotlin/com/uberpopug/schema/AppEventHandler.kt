package com.uberpopug.schema

interface AppEventHandler {

    fun isSuitableFor(event: AppEvent): Boolean
    fun handle(event: AppEvent)
}
