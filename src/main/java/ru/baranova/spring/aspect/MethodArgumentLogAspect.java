package ru.baranova.spring.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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

    //todo
    //@Before("anyMethodArg() || anyServices()")
    @Before("anyMethodArg()")
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
                StringBuilder sbCollection = new StringBuilder();
                //todo
                if (arguments[i].getClass().isArray()) {
                    List<Object> list = new ArrayList<>(Arrays.asList(arguments[i]));
                    for (Object arg : list) {
                        sbCollection.append(arg.toString()).append(", ");
                    }
                    String str = String.format("Arg: %s - Value: %s", methodSignatureParameterNames[i], sbCollection);
                    sb.append(", ").append(str);
                } else if (arguments[i] instanceof Collection) {
                    for (Object arg : (Collection<?>) arguments[i]) {
                        sbCollection.append(arg.toString()).append(", ");
                    }
                    String str = String.format("Arg: %s - Value: %s", methodSignatureParameterNames[i], sbCollection);
                    sb.append(", ").append(str);
                } else {
                    String str = String.format("Arg: %s - Value: %s", methodSignatureParameterNames[i], arguments[i].toString());
                    sb.append(", ").append(str);
                }
            }
        } else {
            sb.append(", No args.");
        }
        log.info("{}", sb);
    }
}
