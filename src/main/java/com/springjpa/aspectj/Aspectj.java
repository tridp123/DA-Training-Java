package com.springjpa.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;


@Aspect
@Configuration
public class Aspectj {
  @Before("execution(* com.springjpa.controller.*.*(..))")
  public void logBefore(JoinPoint joinPoint) {
    System.out.println("before method: " + joinPoint.getSignature().getName());
  }
  


//	@Pointcut("execution(* com.springjpa.controller.*(..))")
//	public void doPointCut() {
//	}
//
//	@Before(value = "doPointCut()", argNames = "joinPoint")
//	public void before(JoinPoint joinPoint) {
//		Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
//		LogUtil.debug(logger, "Starting method " + joinPoint.getSignature().getName() + "().");
//	}
//
//	@After(value = "doPointCut()", argNames = "joinPoint")
//	public void after(JoinPoint joinPoint) {
//		Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
//		LogUtil.debug(logger, "Ending method " + joinPoint.getSignature().getName() + "().");
//	}
}

