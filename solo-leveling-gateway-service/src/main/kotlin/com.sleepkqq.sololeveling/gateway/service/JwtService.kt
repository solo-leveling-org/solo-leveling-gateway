package com.sleepkqq.sololeveling.gateway.service

import com.sleepkqq.sololeveling.gateway.config.JwtProperties
import com.sleepkqq.sololeveling.gateway.model.JwtResponse
import com.sleepkqq.sololeveling.gateway.model.JwtToken
import com.sleepkqq.sololeveling.gateway.model.JwtTokenType
import com.sleepkqq.sololeveling.gateway.model.TgAuthData
import com.sleepkqq.sololeveling.gateway.model.TgUserData
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import java.lang.System.currentTimeMillis
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import javax.crypto.SecretKey

@Service
@EnableConfigurationProperties(JwtProperties::class)
class JwtService {

	companion object {
		const val USERNAME_CLAIM = "username"
		const val FIRST_NAME_CLAIM = "firstName"
		const val LAST_NAME_CLAIM = "lastName"
		const val PHOTO_URL_CLAIM = "photoUrl"
		const val LANGUAGE_CODE_CLAIM = "languageCode"
	}

	private val jwtProperties: JwtProperties
	private val secretKey: SecretKey

	constructor(jwtProperties: JwtProperties) {
		this.jwtProperties = jwtProperties
		this.secretKey = Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray())
	}

	fun generateToken(tgAuthData: TgAuthData): JwtResponse {
		val user = tgAuthData.tgWebAppData.user
		val currentMillis = currentTimeMillis()
		val accessToken = buildToken(user, currentMillis, JwtTokenType.ACCESS)
		val refreshToken = buildToken(user, currentMillis, JwtTokenType.REFRESH)

		return JwtResponse(accessToken, refreshToken)
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

		val expiresAt = LocalDateTime.ofInstant(
			Instant.ofEpochMilli(expirationMillis),
			ZoneId.systemDefault()
		)

		val token = Jwts.builder()
			.subject("${user.id}")
			.claim(USERNAME_CLAIM, user.username)
			.claim(FIRST_NAME_CLAIM, user.firstName)
			.claim(LAST_NAME_CLAIM, user.lastName)
			.claim(PHOTO_URL_CLAIM, user.photoUrl)
			.claim(LANGUAGE_CODE_CLAIM, user.languageCode)
			.issuedAt(Date(currentMillis))
			.expiration(Date(expirationMillis))
			.signWith(secretKey)
			.compact()

		return JwtToken(token, expiresAt, tokenType)
	}

	fun extractClaims(token: String): Claims = Jwts.parser()
		.verifyWith(secretKey)
		.build()
		.parseSignedClaims(token)
		.payload
}
