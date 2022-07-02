package ru.baranova.spring.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class ExecuteTimeAspect {
    @Around("execution(* ru.baranova.spring.services.*.*(..))")
    public Object countMethodExecuteTime(ProceedingJoinPoint pjp) throws Throwable {
        StopWatch watch = new StopWatch();
        watch.start();
        Object retVal = pjp.proceed();
        String className = pjp.getSignature().getDeclaringTypeName();
        String methodName = pjp.getSignature().getName();

        watch.stop();
        log.info("{}", className, methodName, watch.getTotalTimeSeconds()); //?
        System.out.printf("{\nClass: %s\nMethod: %s\nTime, sec: %f%n}\n", className, methodName, watch.getTotalTimeSeconds());
        //log.info("{}", watch.prettyPrint());
        return retVal;
    }
}
