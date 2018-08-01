package com.springjpa.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.springjpa.util.LogUtil;

@Aspect
@Configuration
public class Aspectj {

	@Before("execution(* com.springjpa.*.*(..))")

	/*	@Before("execution(* com.springjpa.controller.*.*(..)) || execution(* com.springjpa.service.*.*(..))")
*/
	public void logBefore(JoinPoint joinPoint) {
		Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
		LogUtil.info(logger,"Before method: " + joinPoint.getSignature().getName());
	}

	@After("execution(* com.springjpa.*.*(..))")
	public void logAfter(JoinPoint joinPoint) {
		Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
		LogUtil.info(logger,"After method: " + joinPoint.getSignature().getName());
	}

}
