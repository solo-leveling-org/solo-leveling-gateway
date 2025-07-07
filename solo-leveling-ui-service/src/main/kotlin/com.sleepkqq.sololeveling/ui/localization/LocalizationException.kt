package com.sleepkqq.sololeveling.ui.localization

class LocalizationException(
	val localizationMessage: LocalizationMessage
) : RuntimeException(localizationMessage.name)