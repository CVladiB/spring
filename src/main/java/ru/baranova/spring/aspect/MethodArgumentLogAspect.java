package ru.baranova.spring.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Aspect
@Component
public class MethodArgumentLogAspect {

    @Pointcut("@annotation(ru.baranova.spring.annotation.MethodArg)")
    public void anyMethodArg() {
    }

    @Pointcut("execution(* ru.baranova.spring.services.*.*(..))")
    public void anyServices() {
    }

    @Before("anyMethodArg() || anyServices()")
    public void loggingMethodArgumentServices(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringTypeName();
        String methodName = methodSignature.getName();

        String[] methodSignatureParameterNames = methodSignature.getParameterNames();
        Object[] arguments = joinPoint.getArgs();

        StringBuilder sb = new StringBuilder();
        sb.append(className).append(".").append(methodName);

        if (arguments.length > 0) {
            for (int i = 0; i < methodSignatureParameterNames.length; i++) {
                Object argument = arguments[i];
                if (!Objects.isNull(argument) && argument.getClass().isArray()) {
                    Object[] array = (Object[]) argument;
                    String args = Arrays.stream(array)
                            .map(Objects::toString)
                            .collect(Collectors.joining(", ", "[", "]"));
                    String str = String.format("Arg: %s - Value: %s", methodSignatureParameterNames[i], args);
                    sb.append(", ").append(str);
                } else if (!Objects.isNull(argument) &&  argument instanceof Collection) {
                    String args = Stream.of(argument)
                            .map(Objects::toString)
                            .collect(Collectors.joining(", ", "[", "]"));
                    String str = String.format("Arg: %s - Value: %s", methodSignatureParameterNames[i], args);
                    sb.append(", ").append(str);
                } else {
                    String str = String.format("Arg: %s - Value: %s", methodSignatureParameterNames[i], argument);
                    sb.append(", ").append(str);
                }
            }
        } else {
            sb.append(", No args.");
        }
        log.info("{}", sb);
    }
}
