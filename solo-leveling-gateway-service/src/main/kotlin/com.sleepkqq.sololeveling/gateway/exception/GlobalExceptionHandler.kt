package com.sleepkqq.sololeveling.gateway.exception

import com.sleepkqq.sololeveling.gateway.dto.ApiExceptionDto
import com.sleepkqq.sololeveling.gateway.localization.LocalizationException
import com.sleepkqq.sololeveling.gateway.localization.LocalizationMessage
import io.grpc.Status
import io.grpc.StatusRuntimeException
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@Suppress("unused")
@RestControllerAdvice
class GlobalExceptionHandler(
	private val messageSource: MessageSource
) {

	private val log = LoggerFactory.getLogger(javaClass)

	@ExceptionHandler(Exception::class)
	fun handleGeneralException(
		e: Exception,
		request: WebRequest
	): ResponseEntity<ApiExceptionDto> {

		log.error("Unexpected error occurred", e)

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(
				ApiExceptionDto(
					status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
					error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
					message = e.toString(),
					path = requestToPath(request)
				)
			)
	}

	@ExceptionHandler(LocalizationException::class)
	fun handleLocalizationException(
		e: LocalizationException,
		request: WebRequest
	): ResponseEntity<ApiExceptionDto> {

		log.error("Localization error occurred", e)

		val httpStatus = when (e.localizationMessage) {
			LocalizationMessage.ERROR_UNEXPECTED -> HttpStatus.INTERNAL_SERVER_ERROR
			LocalizationMessage.ERROR_AUTH_HASH,
			LocalizationMessage.ERROR_AUTH_REQUIRED -> HttpStatus.FORBIDDEN
		}

		val message = messageSource.getMessage(
			e.localizationMessage.path,
			null,
			LocaleContextHolder.getLocale()
		)

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(
				ApiExceptionDto(
					status = httpStatus.value(),
					error = httpStatus.reasonPhrase,
					message = message,
					path = requestToPath(request)
				)
			)
	}

	@ExceptionHandler(StatusRuntimeException::class)
	fun handleGrpcException(
		e: StatusRuntimeException,
		request: WebRequest
	): ResponseEntity<ApiExceptionDto> {

		val grpcStatus = e.status
		val description = grpcStatus.description ?: "gRPC service error"
		val statusCode = grpcStatus.code

		val httpStatus = when (statusCode) {
			Status.Code.NOT_FOUND -> HttpStatus.NOT_FOUND
			Status.Code.INVALID_ARGUMENT -> HttpStatus.BAD_REQUEST
			Status.Code.ALREADY_EXISTS -> HttpStatus.CONFLICT
			Status.Code.PERMISSION_DENIED -> HttpStatus.FORBIDDEN
			Status.Code.UNAUTHENTICATED -> HttpStatus.UNAUTHORIZED
			Status.Code.FAILED_PRECONDITION,
			Status.Code.OUT_OF_RANGE -> HttpStatus.BAD_REQUEST

			Status.Code.UNIMPLEMENTED -> HttpStatus.NOT_IMPLEMENTED
			Status.Code.UNAVAILABLE -> HttpStatus.SERVICE_UNAVAILABLE
			Status.Code.DEADLINE_EXCEEDED -> HttpStatus.GATEWAY_TIMEOUT
			Status.Code.CANCELLED -> HttpStatus.GONE
			else -> HttpStatus.INTERNAL_SERVER_ERROR
		}

		log.error(
			"gRPC call failed: {} (status={}) for request: {}",
			description,
			statusCode,
			requestToPath(request),
			e
		)

		return ResponseEntity.status(httpStatus)
			.body(
				ApiExceptionDto(
					status = httpStatus.value(),
					error = httpStatus.reasonPhrase,
					message = "gRPC service error: $description [${statusCode}]",
					path = requestToPath(request)
				)
			)
	}

	private fun requestToPath(request: WebRequest): String = request
		.getDescription(false)
		.removePrefix("uri=")
}
