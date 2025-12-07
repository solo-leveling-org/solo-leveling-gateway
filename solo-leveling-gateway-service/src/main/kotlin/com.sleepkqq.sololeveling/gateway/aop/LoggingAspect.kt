package com.sleepkqq.sololeveling.gateway.aop

import com.sleepkqq.sololeveling.config.interceptor.UserContextHolder
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.util.StopWatch

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
		val userId = UserContextHolder.getUserId()?.toString() ?: "unauthenticated"

		stopWatch.start()
		val result = try {
			joinPoint.proceed()

		} finally {
			if (stopWatch.isRunning) {
				stopWatch.stop()
			}
		}

		log.info(
			"{} {} '{}' executed by '{}' in {} ms",
			executionType.operationSymbol.value,
			executionType.value,
			joinPoint.signature.name,
			userId,
			stopWatch.totalTimeMillis
		)

		return result
	}
}
