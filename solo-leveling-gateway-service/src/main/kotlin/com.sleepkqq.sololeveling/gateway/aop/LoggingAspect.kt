package com.sleepkqq.sololeveling.gateway.aop

import com.sleepkqq.sololeveling.gateway.model.UserData
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StopWatch

@Suppress("unused")
@Aspect
@Component
class LoggingAspect {

	private val log = LoggerFactory.getLogger(javaClass)

	@Pointcut("within(com.sleepkqq.sololeveling.gateway.controller..*)")
	fun controllersMethods() {
	}

	@Pointcut("within(com.sleepkqq.sololeveling.gateway.grpc.client..*)")
	fun grpcClientsMethods() {
	}

	@Around("controllersMethods()")
	@Throws(Throwable::class)
	fun controllersMethodsLogger(joinPoint: ProceedingJoinPoint): Any =
		logExecutionTime(joinPoint, ExecutionType.REST_API)

	@Around("grpcClientsMethods()")
	@Throws(Throwable::class)
	fun grpcClientsMethodsLogger(joinPoint: ProceedingJoinPoint): Any =
		logExecutionTime(joinPoint, ExecutionType.GRPC_CALL)

	private fun logExecutionTime(
		joinPoint: ProceedingJoinPoint,
		executionType: ExecutionType
	): Any {
		val stopWatch = StopWatch()
		val principal = getCurrentUser()

		val operationSymbol = when (executionType) {
			ExecutionType.REST_API -> "<<"
			ExecutionType.GRPC_CALL -> ">>"
		}

		stopWatch.start()
		val result = try {
			joinPoint.proceed()

		} catch (e: Throwable) {
			stopWatch.stop()

			log.error(
				"{} {} '{}' failed for user '{}' in {} ms",
				operationSymbol,
				executionType.value,
				joinPoint.signature.name,
				principal,
				stopWatch.totalTimeMillis,
				e
			)
			throw e
		} finally {
			if (stopWatch.isRunning) {
				stopWatch.stop()
			}
		}

		log.info(
			"{} {} '{}' executed by '{}' in {} ms",
			operationSymbol,
			executionType.value,
			joinPoint.signature.name,
			principal,
			stopWatch.totalTimeMillis
		)

		return result
	}

	private fun getCurrentUser(): String {
		val authentication = SecurityContextHolder.getContext().authentication
			?: return "unauthenticated"
		val principal = authentication.principal

		return if (principal is UserData) {
			principal.id.toString()
		} else {
			"anonymous"
		}
	}
}
