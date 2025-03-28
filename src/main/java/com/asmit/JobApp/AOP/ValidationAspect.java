package com.asmit.JobApp.AOP;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Component
public class ValidationAspect
{
    private static final Logger logger = LoggerFactory.getLogger(ValidationAspect.class);
    
    @Around("execution(* com.asmit.JobApp.service.JobService.*ById(..)) && args(postId)")
    public Object validateInputs(ProceedingJoinPoint joinPoint, int postId) throws Throwable
    {
        if (postId<=0)
        {
            logger.error("Validation failed: postId cannot be zero or negative");
            throw new IllegalArgumentException("postId cannot be zero or negative");
        }
        else
        {
            Object result = joinPoint.proceed();
            logger.info("Validation passed for request");
            return result;
        }
    }
}
