package ru.baranova.spring.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import ru.baranova.spring.config.BusinessConstants;

import javax.persistence.PersistenceException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Aspect
@Component
public class ThrowingAspect {

    @Around("allMethodsDaoEntity()")
    public Object appDataAccessExceptionHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (DataAccessException | PersistenceException e) {
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
            Class returnType = joinPoint
                    .getSourceLocation()
                    .getWithinType()
                    .getMethod(joinPoint.getSignature().getName(), args)
                    .getReturnType();

            if (returnType == Boolean.class) {
                returnValueIfException = Boolean.FALSE;
            } else if (returnType == List.class) {
                returnValueIfException = Collections.emptyList();
            }

            return returnValueIfException;
        }
    }

    @AfterReturning(value = "allMethodsServiceData()"
            , returning = "result")
    public void serviceNPEMaker(Object result) {
        if (result == null || result.equals(Collections.emptyList()) || result.equals(Boolean.FALSE)) {
            throw new NullPointerException(BusinessConstants.ShellEntityServiceLog.WARNING);
        }
    }

    @Around("allMethodsController()")
    public Object controllersNPEHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (NullPointerException e) {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
    }

    @Pointcut("execution(* ru.baranova.spring.repository.entity..*(..))")
    private void allMethodsDaoEntity() {
    }

    @Pointcut("execution(* ru.baranova.spring.service.data..*(..))")
    private void allMethodsServiceData() {
    }

    @Pointcut("execution(* ru.baranova.spring.controller..*(..))")
    private void allMethodsController() {
    }
}
