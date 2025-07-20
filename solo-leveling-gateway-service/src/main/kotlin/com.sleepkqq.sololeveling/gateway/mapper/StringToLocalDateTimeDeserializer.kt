package com.sleepkqq.sololeveling.gateway.mapper

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId


class StringToLocalDateTimeDeserializer : JsonDeserializer<LocalDateTime>() {

	override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): LocalDateTime {
		val value = p.valueAsString
		try {
			val timestamp = value.toLong()
			return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault())
		} catch (_: NumberFormatException) {
			return LocalDateTime.parse(value)
		}
	}
}
