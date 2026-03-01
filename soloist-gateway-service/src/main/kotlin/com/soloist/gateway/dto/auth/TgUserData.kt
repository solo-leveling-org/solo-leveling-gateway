package com.soloist.gateway.dto.auth

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
	val isPremium: Boolean?,
)
