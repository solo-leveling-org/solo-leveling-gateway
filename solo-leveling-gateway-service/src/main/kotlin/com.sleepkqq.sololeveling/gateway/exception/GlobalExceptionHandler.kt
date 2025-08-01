package com.sleepkqq.sololeveling.gateway.exception

import com.sleepkqq.sololeveling.gateway.dto.ApiExceptionDto
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@Suppress("unused")
@RestControllerAdvice
class GlobalExceptionHandler {

	private val log = LoggerFactory.getLogger(javaClass)

	@ExceptionHandler(Exception::class)
	fun handleGeneralException(
		e: Exception,
		request: WebRequest
	): ResponseEntity<ApiExceptionDto> {

		log.error("Unexpected error occurred: {}", e.message, e)

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(
				ApiExceptionDto(
					status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
					error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
					message = e.toString(),
					path = request.getDescription(false).removePrefix("uri=")
				)
			)
	}
}
