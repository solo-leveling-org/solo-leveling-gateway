package com.soloist.gateway.service.auth

import com.soloist.gateway.config.properties.JwtProperties
import com.soloist.gateway.dto.auth.JwtToken
import com.soloist.gateway.dto.auth.JwtTokenType
import com.soloist.gateway.dto.auth.LoginResponse
import com.soloist.gateway.dto.auth.TgUserData
import com.soloist.gateway.extensions.toTgUser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service
import java.lang.System.currentTimeMillis
import java.time.Instant
import java.time.OffsetDateTime
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtService {

	companion object {
		const val USERNAME_CLAIM = "username"
		const val FIRST_NAME_CLAIM = "firstName"
		const val LAST_NAME_CLAIM = "lastName"
		const val PHOTO_URL_CLAIM = "photoUrl"
		const val LANGUAGE_CODE_CLAIM = "languageCode"
		const val ADDED_TO_ATTACHMENT_MENU_CLAIM = "addedToAttachmentMenu"
		const val ALLOWS_WRITE_TO_PM_CLAIM = "allowsWriteToPm"
		const val IS_BOT_CLAIM = "isBot"
		const val IS_PREMIUM_CLAIM = "isPremium"
	}

	private val jwtProperties: JwtProperties
	private val secretKey: SecretKey

	constructor(jwtProperties: JwtProperties) {
		this.jwtProperties = jwtProperties
		this.secretKey = Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray())
	}

	fun generateToken(user: TgUserData): LoginResponse {
		val currentMillis = currentTimeMillis()
		val accessToken = buildToken(user, currentMillis, JwtTokenType.ACCESS)
		val refreshToken = buildToken(user, currentMillis, JwtTokenType.REFRESH)

		return LoginResponse(accessToken, refreshToken)
	}

	private fun buildToken(
		user: TgUserData,
		currentMillis: Long,
		tokenType: JwtTokenType
	): JwtToken {
		val expirationMillis = currentMillis + when (tokenType) {
			JwtTokenType.ACCESS -> jwtProperties.accessLifetime
			JwtTokenType.REFRESH -> jwtProperties.refreshLifetime
		}

		val expiresAt = OffsetDateTime.ofInstant(
			Instant.ofEpochMilli(expirationMillis),
			LocaleContextHolder.getTimeZone().toZoneId()
		)

		val token = Jwts.builder()
			.subject("${user.id}")
			.claim(USERNAME_CLAIM, user.username)
			.claim(FIRST_NAME_CLAIM, user.firstName)
			.claim(LAST_NAME_CLAIM, user.lastName)
			.claim(PHOTO_URL_CLAIM, user.photoUrl)
			.claim(LANGUAGE_CODE_CLAIM, user.languageCode)
			.claim(ADDED_TO_ATTACHMENT_MENU_CLAIM, user.addedToAttachmentMenu)
			.claim(ALLOWS_WRITE_TO_PM_CLAIM, user.allowsWriteToPm)
			.claim(IS_BOT_CLAIM, user.isBot)
			.claim(IS_PREMIUM_CLAIM, user.isPremium)
			.issuedAt(Date(currentMillis))
			.expiration(Date(expirationMillis))
			.signWith(secretKey)
			.compact()

		return JwtToken(token, expiresAt, tokenType)
	}

	fun extractTgUser(token: String): TgUserData = Jwts.parser()
		.verifyWith(secretKey)
		.build()
		.parseSignedClaims(token)
		.payload
		.toTgUser()

	fun generateAccessTokenFromRefreshToken(refreshToken: String): JwtToken =
		buildToken(
			extractTgUser(refreshToken),
			currentTimeMillis(),
			JwtTokenType.ACCESS
		)
}
