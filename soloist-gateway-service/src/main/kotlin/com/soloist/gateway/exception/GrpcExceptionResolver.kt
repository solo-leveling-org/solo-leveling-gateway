package com.soloist.gateway.exception

import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import io.grpc.StatusException
import io.grpc.StatusRuntimeException
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter
import org.springframework.graphql.execution.ErrorType
import org.springframework.stereotype.Component

@Component
class GrpcExceptionResolver : DataFetcherExceptionResolverAdapter() {

	override fun resolveToSingleError(
		ex: Throwable,
		env: DataFetchingEnvironment
	): GraphQLError? {
		val (grpcStatus, message) = when (ex) {
			is StatusRuntimeException -> ex.status to (ex.status.description ?: ex.message
			?: "gRPC error")

			is StatusException -> ex.status to (ex.status.description ?: ex.message ?: "gRPC error")
			else -> return null
		}

		val errorType = when (grpcStatus.code) {
			io.grpc.Status.Code.NOT_FOUND -> ErrorType.NOT_FOUND
			io.grpc.Status.Code.UNAUTHENTICATED -> ErrorType.UNAUTHORIZED
			io.grpc.Status.Code.PERMISSION_DENIED -> ErrorType.FORBIDDEN
			io.grpc.Status.Code.INVALID_ARGUMENT -> ErrorType.BAD_REQUEST
			else -> ErrorType.INTERNAL_ERROR
		}

		return GraphqlErrorBuilder.newError(env)
			.message(message)
			.errorType(errorType)
			.extensions(
				mapOf(
					"grpcStatus" to grpcStatus.code.name,
					"grpcDescription" to message
				)
			)
			.build()
	}
}
