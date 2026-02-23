package com.soloist.gateway.localization

class LocalizationException(
	val localizationMessage: LocalizationMessage
) : RuntimeException(localizationMessage.path)
