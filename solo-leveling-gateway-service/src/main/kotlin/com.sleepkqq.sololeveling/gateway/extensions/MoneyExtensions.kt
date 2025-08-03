package com.sleepkqq.sololeveling.gateway.extensions

import com.google.type.Money
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.abs

fun Money.toBigDecimal(): BigDecimal {
	val units = this.units
	val nanos = this.nanos

	// Определяем общий знак: если units < 0 или (units == 0 и nanos < 0)
	val isNegative = units < 0 || (units == 0L && nanos < 0)

	val decimalUnits = BigDecimal.valueOf(abs(units))
	val decimalNanos = BigDecimal(abs(nanos))
		.setScale(9) // 9 знаков после запятой
		.divide(BigDecimal(1_000_000_000), RoundingMode.UNNECESSARY)

	val result = decimalUnits + decimalNanos
	return if (isNegative) {
		result.negate()
	} else {
		result
	}
}
