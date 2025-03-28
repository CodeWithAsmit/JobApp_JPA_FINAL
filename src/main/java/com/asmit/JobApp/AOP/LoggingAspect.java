package com.asmit.JobApp.AOP;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect 
{
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.asmit.JobApp.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint)
    {
        logger.info("Method {} execution started...", joinPoint.getSignature().getName());

    }

    @After("execution(* com.asmit.JobApp.service.*.*(..))")
    public void logAfter(JoinPoint joinPoint)
    {
        logger.info("Method {} execution completed...", joinPoint.getSignature().getName());
    }
}

