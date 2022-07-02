package ru.baranova.spring.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
//@Component
public class ExecuteTimeAspect {

    @Around("execution(* ru.baranova.spring.services.TestServiceImpl.*())")
    public Object methodExecuteTime(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("Aspect start");
        StopWatch watch = new StopWatch();
        watch.start();

        Object retVal = pjp.proceed();

        watch.stop();
        log.info("{}", watch.prettyPrint());
        return retVal;
    }
}
