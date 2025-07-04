package com.sleepkqq.sololeveling.ui.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TgUserData(
	val id: Long,
	val username: String?,
	val firstName: String?,
	val lastName: String?,
	val photoUrl: String?,
	val languageCode: String?,
	val addedToAttachmentMenu: Boolean?,
	val allowsWriteToPm: Boolean?,
	val isBot: Boolean?,
	val isPremium: Boolean?
)
