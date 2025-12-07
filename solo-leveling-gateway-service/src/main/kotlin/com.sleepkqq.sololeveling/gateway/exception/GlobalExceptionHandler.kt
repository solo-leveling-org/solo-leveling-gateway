package com.sleepkqq.sololeveling.gateway.exception

import com.sleepkqq.sololeveling.gateway.dto.ApiExceptionDto
import com.sleepkqq.sololeveling.gateway.localization.LocalizationException
import com.sleepkqq.sololeveling.gateway.localization.LocalizationMessage
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.jsonwebtoken.ExpiredJwtException
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
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

	@ExceptionHandler(ExpiredJwtException::class, BadCredentialsException::class)
	fun handleAuthException(
		e: Exception,
		request: WebRequest
	): ResponseEntity<ApiExceptionDto> =
		ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.body(
				ApiExceptionDto(
					status = HttpStatus.UNAUTHORIZED.value(),
					error = HttpStatus.UNAUTHORIZED.reasonPhrase,
					message = e.toString(),
					path = requestToPath(request)
				)
			)

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
		val requestPath = requestToPath(request)

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

		when (statusCode) {
			Status.Code.NOT_FOUND,
			Status.Code.INVALID_ARGUMENT,
			Status.Code.ALREADY_EXISTS,
			Status.Code.PERMISSION_DENIED,
			Status.Code.UNAUTHENTICATED,
			Status.Code.FAILED_PRECONDITION,
			Status.Code.OUT_OF_RANGE -> {
				if (log.isDebugEnabled) {
					log.debug(
						"gRPC client error: {} (status={}) for request: {}",
						description,
						statusCode,
						requestPath
					)
				}
			}

			Status.Code.UNAVAILABLE,
			Status.Code.DEADLINE_EXCEEDED,
			Status.Code.CANCELLED -> {
				log.warn(
					"gRPC operational issue: {} (status={}) for request: {}",
					description,
					statusCode,
					requestPath
				)
			}

			else -> {
				log.error(
					"gRPC server error: {} (status={}) for request: {}",
					description,
					statusCode,
					requestPath,
					e
				)
			}
		}

		return ResponseEntity.status(httpStatus)
			.body(
				ApiExceptionDto(
					status = httpStatus.value(),
					error = httpStatus.reasonPhrase,
					message = "gRPC service error: $description [${statusCode}]",
					path = requestPath
				)
			)
	}

	private fun requestToPath(request: WebRequest): String = request
		.getDescription(false)
		.removePrefix("uri=")
}
