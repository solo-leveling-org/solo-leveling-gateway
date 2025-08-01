package com.sleepkqq.sololeveling.gateway.localization

class LocalizationException(
	localizationMessage: LocalizationMessage
) : RuntimeException(localizationMessage.name)
