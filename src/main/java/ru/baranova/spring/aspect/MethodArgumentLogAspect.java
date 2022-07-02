package ru.baranova.spring.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class MethodArgumentLogAspect {

    @Before("execution(* ru.baranova.spring.services.*.*(..))")
    public void loggingMethodArgumentServices (JoinPoint joinPoint) {
        Object[] arguments = joinPoint.getArgs();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        System.out.printf("{Class: %s, Method: %s }\n", className, methodName);
        for(Object argument : arguments) {
            System.out.printf("{Arg: %s }\n", argument.getClass());
        }
    }
}
