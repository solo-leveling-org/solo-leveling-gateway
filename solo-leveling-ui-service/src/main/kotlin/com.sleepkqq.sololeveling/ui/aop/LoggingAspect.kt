package com.sleepkqq.sololeveling.ui.aop

import com.sleepkqq.sololeveling.ui.service.TgAuthService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.util.StopWatch

@Aspect
@Component
class LoggingAspect(
	private val tgAuthService: TgAuthService
) {

	private val log = LoggerFactory.getLogger(LoggingAspect::class.java)

	@Pointcut("within(com.sleepkqq.sololeveling.ui.api..*)")
	fun apiMethods() {

	}

	@Around("apiMethods()")
	@Throws(Throwable::class)
	fun logExecutionTime(joinPoint: ProceedingJoinPoint): Any {
		val username = tgAuthService.findCurrentUser()
			?.username
			?: "anon"

		val stopWatch = StopWatch()
		stopWatch.start()
		val result = joinPoint.proceed()
		stopWatch.stop()

		log.info(
			">> api '{}' executed by '{}' in {} ms",
			joinPoint.signature.name,
			username,
			stopWatch.totalTimeMillis
		)

		return result
	}
}