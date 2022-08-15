package ru.baranova.spring.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import ru.baranova.spring.domain.BusinessConstants;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class ThrowingAspect {

    @Pointcut("execution(* ru.baranova.spring.dao..*(..))")
    public void allMethodsDaoApp() {
    }

    @Pointcut("execution(* ru.baranova.spring.controller..*(..))")
    public void allMethodsControllers() {
    }

    @Pointcut("execution(* ru.baranova.spring.service..*(..))")
    public void allMethodsService() {
    }

    @Around("allMethodsDaoApp()")
    public Object appDataAccessExceptionHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (DataAccessException e) {
            log.info(BusinessConstants.EntityServiceLog.WARNING_NEED_ADMINISTRATOR);

            Object returnValueIfException = null;

            Class[] args = Arrays.stream(joinPoint.getArgs())
                    .<Class>map(Object::getClass)
                    .toList()
                    .toArray(new Class[0]);
            Class returnType = joinPoint
                    .getSourceLocation()
                    .getWithinType()
                    .getMethod(joinPoint.getSignature().getName(), args)
                    .getReturnType();

            if (returnType == Boolean.class) {
                returnValueIfException = Boolean.FALSE;
            } else if (returnType == List.class) {
                returnValueIfException = Collections.emptyList();
            } else if (returnType == Map.class) {
                returnValueIfException = Collections.emptyMap();
            } else if (returnType == Array.class) {
                returnValueIfException = new Object[0];
            }

            return returnValueIfException;
        }
    }

    @Around("allMethodsControllers()")
    public Object controllersNPEHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (NullPointerException e) {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
    }

    @AfterReturning(pointcut = "allMethodsService()", returning = "result")
    public void serviceNPEMaker(Object result) {
        if (result == null
                || result.equals(Collections.emptyList())
                || result.equals(Boolean.FALSE)) {
            throw new NullPointerException(BusinessConstants.ShellEntityServiceLog.WARNING);
        }
    }
}
