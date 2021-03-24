package com.uberpopug.schema

import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.OffsetDateTime
import java.util.UUID

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, property = "type")
abstract class AppEvent(val meta: EventMetaData)

data class EventMetaData(
    val eventName: String,
    val eventVersion: Int,
    val producer: String,
    val eventId: String = UUID.randomUUID().toString(),
    val eventTime: OffsetDateTime = OffsetDateTime.now()
)
