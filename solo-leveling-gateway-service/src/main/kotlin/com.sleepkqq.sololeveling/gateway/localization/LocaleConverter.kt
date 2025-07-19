package com.sleepkqq.sololeveling.gateway.localization

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.util.Locale

@Converter(autoApply = true)
class LocaleConverter : AttributeConverter<Locale, String> {

	override fun convertToDatabaseColumn(locale: Locale): String = locale.toLanguageTag()

	override fun convertToEntityAttribute(dbData: String): Locale =
		Locale.forLanguageTag(dbData)
}
