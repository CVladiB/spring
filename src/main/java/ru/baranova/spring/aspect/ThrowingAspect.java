package ru.baranova.spring.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import ru.baranova.spring.domain.BusinessConstants;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

@Slf4j
@Aspect
@Component
public class ThrowingAspect {

    @Around("execution(* ru.baranova.spring.dao..*(..))")
    public Object appDataAccessExceptionHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (DataAccessException e) {
            if (e.getMessage().equals(BusinessConstants.DaoLog.NOTHING_IN_BD)
                    || e.getMessage().equals(BusinessConstants.DaoLog.SHOULD_EXIST_INPUT)) {
                log.info(e.getMessage());
            } else {
                log.info(BusinessConstants.EntityServiceLog.WARNING_NEED_ADMINISTRATOR);
            }

            Object returnValueIfException = null;

            Class[] args = Arrays.stream(joinPoint.getArgs())
                    .<Class>map(Object::getClass)
                    .toList()
                    .toArray(new Class[0]);
            Method method = joinPoint
                    .getSourceLocation()
                    .getWithinType()
                    .getMethod(joinPoint.getSignature().getName(), args);
            Class returnType = method.getReturnType();

            if (returnType == Boolean.class) {
                returnValueIfException = Boolean.FALSE;
            }

            return returnValueIfException;
        }
    }

    @AfterReturning(pointcut = "execution(* ru.baranova.spring.service.data..*(..))" +
            "execution(* ru.baranova.spring.service.app..*(..))"
            , returning = "result")
    public void serviceNPEMaker(Object result) {
        if (result == null || result.equals(Collections.emptyList()) || result.equals(Boolean.FALSE)) {
            throw new NullPointerException(BusinessConstants.ShellEntityServiceLog.WARNING);
        }
    }

    @Around("execution(* ru.baranova.spring.controller..*(..))")
    public Object controllersNPEHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (NullPointerException e) {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
    }
}
