package com.sleepkqq.sololeveling.gateway.extensions

import com.sleepkqq.sololeveling.gateway.model.TgUserData
import com.sleepkqq.sololeveling.gateway.service.JwtService
import io.jsonwebtoken.Claims

fun Claims.getUsername(): String? = get(JwtService.USERNAME_CLAIM, String::class.java)
fun Claims.getFirstName(): String? = get(JwtService.FIRST_NAME_CLAIM, String::class.java)
fun Claims.getLastName(): String? = get(JwtService.LAST_NAME_CLAIM, String::class.java)
fun Claims.getPhotoUrl(): String? = get(JwtService.PHOTO_URL_CLAIM, String::class.java)
fun Claims.getLanguageCode(): String? = get(JwtService.LANGUAGE_CODE_CLAIM, String::class.java)
fun Claims.getAddedToAttachmentMenu(): Boolean? =
	get(JwtService.ADDED_TO_ATTACHMENT_MENU_CLAIM, Boolean::class.java)

fun Claims.getAllowsWriteToPm(): Boolean? =
	get(JwtService.ALLOWS_WRITE_TO_PM_CLAIM, Boolean::class.java)

fun Claims.getIsBot(): Boolean? = get(JwtService.IS_BOT_CLAIM, Boolean::class.java)
fun Claims.getIsPremium(): Boolean? = get(JwtService.IS_PREMIUM_CLAIM, Boolean::class.java)

fun Claims.toTgUser(): TgUserData = TgUserData(
	id = subject.toLong(),
	username = getUsername(),
	firstName = getFirstName(),
	lastName = getLastName(),
	photoUrl = getPhotoUrl(),
	languageCode = getLanguageCode(),
	addedToAttachmentMenu = getAddedToAttachmentMenu(),
	allowsWriteToPm = getAllowsWriteToPm(),
	isBot = getIsBot(),
	isPremium = getIsPremium()
)
