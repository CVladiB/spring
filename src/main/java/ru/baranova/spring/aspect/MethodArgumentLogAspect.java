package ru.baranova.spring.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class MethodArgumentLogAspect {

/*    @Before("execution(* ru.baranova.spring.services.*.*(..))")
    public void loggingMethodArgumentServices(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringTypeName();
        String methodName = methodSignature.getName();

        String[] methodSignatureParameterNames = methodSignature.getParameterNames();
        Object[] arguments = joinPoint.getArgs();

        StringBuilder sb = new StringBuilder();
        sb.append(className).append(".").append(methodName);

        if (arguments.length > 0) {
            for (int i = 0; (i < methodSignatureParameterNames.length) && (i < arguments.length); i++) {
                String str = String.format("Arg: %s - Value: %s", methodSignatureParameterNames[i], arguments[i]);
                sb.append(", ").append(str);
            }
        } else {
            sb.append(", No args.");
        }
        log.info("{}", sb);
    }*/

    @Before("@annotation(ru.baranova.spring.annotation.MethodArg)")
    public void loggingMethodArgumentMethodArg(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringTypeName();
        String methodName = methodSignature.getName();

        String[] methodSignatureParameterNames = methodSignature.getParameterNames();
        Object[] arguments = joinPoint.getArgs();

        StringBuilder sb = new StringBuilder();
        sb.append(className).append(".").append(methodName);

        if (arguments.length > 0) {
            for (int i = 0; (i < methodSignatureParameterNames.length) && (i < arguments.length); i++) {
                String str = String.format("Arg: %s - Value: %s", methodSignatureParameterNames[i], arguments[i]);
                sb.append(", ").append(str);
            }
        } else {
            sb.append(", No args.");
        }
        log.info("{}", sb);
    }
}
