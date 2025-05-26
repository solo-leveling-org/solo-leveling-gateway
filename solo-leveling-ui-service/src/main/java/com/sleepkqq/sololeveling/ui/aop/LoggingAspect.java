package com.sleepkqq.sololeveling.ui.aop;

import com.sleepkqq.sololeveling.ui.model.UserData;
import com.sleepkqq.sololeveling.ui.service.TgAuthService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class LoggingAspect {

  private final TgAuthService tgAuthService;

  @Pointcut("within(com.sleepkqq.sololeveling.ui.api..*)")
  public void apiMethods() {}

  @Around("apiMethods()")
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    var username = tgAuthService.findCurrentUser().map(UserData::getUsername).orElse("anon");
    var stopWatch = new StopWatch();

    stopWatch.start();
    var result = joinPoint.proceed();
    stopWatch.stop();

    log.info(
        ">> api '{}' executed by '{}' in {} ms",
        joinPoint.getSignature().getName(),
        username,
        stopWatch.getTotalTimeMillis()
    );

    return result;
  }
}