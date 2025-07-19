package com.sleepkqq.sololeveling.gateway.localization

class LocalizationException(
	val localizationMessage: LocalizationMessage
) : RuntimeException(localizationMessage.name)