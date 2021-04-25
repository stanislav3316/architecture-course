package com.uberpopug.analytics.data

import com.fasterxml.jackson.databind.ObjectMapper
import com.uberpopug.schema.AppEvent
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class RawEventRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    fun save(event: AppEvent) {

        val sql = """
            INSERT INTO raw_event (
                event_id,
                event_name,
                event_version,
                producer,
                event_time,
                raw
            ) VALUES (
                :event_id,
                :event_name,
                :event_version,
                :producer,
                :event_time,
                :raw::JSONB
            )
        """.trimIndent()

        val params = MapSqlParameterSource()
            .addValue("event_id", event.meta.eventId)
            .addValue("event_name", event.meta.eventName)
            .addValue("event_version", event.meta.eventVersion)
            .addValue("producer", event.meta.producer)
            .addValue("event_time", event.meta.eventTime)
            .addValue("raw", objectMapper.writeValueAsString(event))

        jdbcTemplate.update(sql, params)
    }
}
