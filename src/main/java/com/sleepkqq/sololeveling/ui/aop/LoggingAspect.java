package com.sleepkqq.sololeveling.ui.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

  @Pointcut("within(com.sleepkqq.sololeveling.ui.api..*)")
  public void apiMethods() {}

  @Around("apiMethods()")
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    var stopWatch = new StopWatch();

    stopWatch.start();
    var result = joinPoint.proceed();
    stopWatch.stop();

    log.info(
        "api '{}' executed in {} ms",
        joinPoint.getSignature().getName(),
        stopWatch.getTotalTimeMillis()
    );

    return result;
  }
}