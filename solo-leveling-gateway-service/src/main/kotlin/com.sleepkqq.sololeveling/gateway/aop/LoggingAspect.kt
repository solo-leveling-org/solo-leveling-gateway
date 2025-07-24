package com.sleepkqq.sololeveling.gateway.aop

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

	@Around("controllerMethods()")
	@Throws(Throwable::class)
	fun controllersMethodsLogger(joinPoint: ProceedingJoinPoint): Any =
		logExecutionTime(joinPoint, ExecutionType.REST_API)

	@Pointcut("within(com.sleepkqq.sololeveling.gateway.grpc.client..*)")
	fun grpcClientsMethods() {

	}

	@Around("grpcClientsMethods()")
	@Throws(Throwable::class)
	fun grpcClientsMethodsLogger(joinPoint: ProceedingJoinPoint): Any =
		logExecutionTime(joinPoint, ExecutionType.GRPC_CALL)

	private fun logExecutionTime(
		joinPoint: ProceedingJoinPoint,
		executionType: ExecutionType
	): Any {
		val stopWatch = StopWatch()

		stopWatch.start()
		val result = joinPoint.proceed()
		stopWatch.stop()

		log.info(
			">> {} '{}' executed by in {} ms",
			executionType.value,
			joinPoint.signature.name,
			stopWatch.totalTimeMillis
		)

		return result
	}
}