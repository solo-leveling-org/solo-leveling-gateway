package com.soloist.gateway.dto

data class ApiExceptionDto(
	val status: Int,
	val error: String,
	val message: String,
	val path: String
)
