package com.sleepkqq.sololeveling.gateway.extensions

import com.sleepkqq.sololeveling.gateway.dto.RestTgUserData
import com.sleepkqq.sololeveling.gateway.service.auth.JwtService
import io.jsonwebtoken.Claims

fun Claims.extractUsername(): String? = get(JwtService.USERNAME_CLAIM, String::class.java)

fun Claims.extractFirstName(): String? = get(JwtService.FIRST_NAME_CLAIM, String::class.java)

fun Claims.extractLastName(): String? = get(JwtService.LAST_NAME_CLAIM, String::class.java)

fun Claims.extractPhotoUrl(): String? = get(JwtService.PHOTO_URL_CLAIM, String::class.java)

fun Claims.extractLanguageCode(): String? = get(JwtService.LANGUAGE_CODE_CLAIM, String::class.java)

fun Claims.extractAddedToAttachmentMenu(): Boolean? =
	get(JwtService.ADDED_TO_ATTACHMENT_MENU_CLAIM, Boolean::class.javaObjectType)

fun Claims.extractAllowsWriteToPm(): Boolean? =
	get(JwtService.ALLOWS_WRITE_TO_PM_CLAIM, Boolean::class.javaObjectType)

fun Claims.extractIsBot(): Boolean? =
	get(JwtService.IS_BOT_CLAIM, Boolean::class.javaObjectType)

fun Claims.extractIsPremium(): Boolean? =
	get(JwtService.IS_PREMIUM_CLAIM, Boolean::class.javaObjectType)

fun Claims.toTgUser(): RestTgUserData = RestTgUserData(subject.toLong())
	.apply {
		username = extractUsername()
		firstName = extractFirstName()
		lastName = extractLastName()
		photoUrl = extractPhotoUrl()
		languageCode = extractLanguageCode()
		addedToAttachmentMenu = extractAddedToAttachmentMenu()
		allowsWriteToPm = extractAllowsWriteToPm()
		isBot = extractIsBot()
		isPremium = extractIsPremium()
	}
