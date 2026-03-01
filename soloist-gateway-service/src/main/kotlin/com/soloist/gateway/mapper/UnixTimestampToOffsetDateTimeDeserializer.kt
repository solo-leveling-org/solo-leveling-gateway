package com.soloist.gateway.mapper

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

class UnixTimestampToOffsetDateTimeDeserializer : JsonDeserializer<OffsetDateTime>() {

	override fun deserialize(p: JsonParser, ctxt: DeserializationContext): OffsetDateTime {
		val value = p.valueAsString
		return try {
			val timestamp = value.toLong()
			OffsetDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneOffset.UTC)

		} catch (_: NumberFormatException) {
			OffsetDateTime.parse(value)
		}
	}
}
