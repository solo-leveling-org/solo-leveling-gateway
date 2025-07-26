package com.sleepkqq.sololeveling.gateway.exception

import com.sleepkqq.sololeveling.gateway.dto.ApiException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class GlobalExceptionHandler {

	@ExceptionHandler(Exception::class)
	fun handleGeneralException(
		ex: Exception,
		request: WebRequest
	): ResponseEntity<ApiException> {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(
				ApiException(
					status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
					error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
					message = "Internal server error",
					path = request.getDescription(false).removePrefix("uri=")
				)
			)
	}
}
