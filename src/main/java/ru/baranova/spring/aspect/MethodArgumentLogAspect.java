package ru.baranova.spring.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Aspect
@Component
public class MethodArgumentLogAspect {
    MethodSignature methodSignature;
    String className;
    String methodName;
    String[] methodSignatureParameterNames;
    Object[] arguments;

    @Pointcut("@annotation(ru.baranova.spring.annotation.MethodArg)")
    public void anyMethodArg() {
    }

    @Pointcut("execution(* ru.baranova.spring.services.*.*(..))")
    public void anyServices() {
    }

    @Before("anyMethodArg() || anyServices()")
    public void loggingMethodArgumentServices(JoinPoint joinPoint) {
        methodSignature = (MethodSignature) joinPoint.getSignature();
        className = methodSignature.getDeclaringTypeName();
        methodName = methodSignature.getName();

        methodSignatureParameterNames = methodSignature.getParameterNames();
        arguments = joinPoint.getArgs();

        StringBuilder sb = new StringBuilder();
        sb.append(className).append(".").append(methodName);

        if (arguments.length > 0) {
            for (int i = 0; i < methodSignatureParameterNames.length; i++) {
                if (arguments[i] instanceof Collection) {
                    String argumentsCollection;
                    StringBuilder sbCollection = new StringBuilder();
                    for (Object arg : (Collection<?>) arguments[i]) {
                        sbCollection.append(arg).append(", ");
                    }
                    String str = String.format("Arg: %s - Value: %s", methodSignatureParameterNames[i], sbCollection);
                    sb.append(", ").append(str);
                } else {
                    String str = String.format("Arg: %s - Value: %s", methodSignatureParameterNames[i], arguments[i]);
                    sb.append(", ").append(str);
                }
            }
        } else {
            sb.append(", No args.");
        }
        log.info("{}", sb);
    }
}
