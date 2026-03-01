package com.soloist.gateway.dto.auth

data class LoginResponse(
	val accessToken: JwtToken,
	val refreshToken: JwtToken
)
