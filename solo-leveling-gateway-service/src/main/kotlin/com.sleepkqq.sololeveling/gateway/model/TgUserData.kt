package com.sleepkqq.sololeveling.gateway.model

import com.fasterxml.jackson.annotation.JsonProperty
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
	@JsonProperty("is_bot")
	val isBot: Boolean?,
	@JsonProperty("is_premium")
	val isPremium: Boolean?
) {

	constructor() : this(1, null, null, null, null, null, false, false, false, false)
}
