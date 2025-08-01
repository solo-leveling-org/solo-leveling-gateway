package com.sleepkqq.sololeveling.gateway.dto

data class ApiExceptionDto(
	val status: Int,
	val error: String,
	val message: String,
	val path: String
)
