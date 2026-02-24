package com.soloist.gateway.extensions

import com.google.protobuf.Timestamp
import org.springframework.context.i18n.LocaleContextHolder
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime

fun Timestamp.toOffsetDateTime(): OffsetDateTime? {
	if (this == Timestamp.getDefaultInstance()) {
		return null
	}

	return OffsetDateTime.ofInstant(
		Instant.ofEpochSecond(this.seconds, this.nanos.toLong()),
		LocaleContextHolder.getTimeZone().toZoneId()
	)
}

fun LocalDate.toTimestamp(): Timestamp {
	val zoneId = LocaleContextHolder.getTimeZone().toZoneId()
	val instant = this.atStartOfDay(zoneId).toInstant()
	return Timestamp.newBuilder()
		.setSeconds(instant.epochSecond)
		.setNanos(instant.nano)
		.build()
}
