/*
 * Copyright 2011 Janis Kazakovs <janis.kazakovs@opatopa.com>.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.devtoolbox.logging.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.devtoolbox.logging.aspect.annotation.Loggable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Janis Kazakovs <janis.kazakovs@opatopa.com>
 * 
 */
public @Aspect class LoggingAspect {
	@Pointcut("within(com.company.service.*) && execution(* *(..)) && @annotation(loggable)")
	public void logging(Loggable loggable) {
	}

	@Before(value = "logging(loggable)", argNames = "joinPoint,loggable")
	public void enteringMethod(JoinPoint joinPoint, Loggable loggable) {
		Signature signature = joinPoint.getSignature();
		String className = signature.getDeclaringType().getSimpleName();
		String methodName = signature.getName();
		logger.debug("enteringMethod {}::{}", className, methodName);
	}

	@AfterReturning(pointcut = "logging(loggable)", returning = "returnValue", argNames = "joinPoint,loggable,returnValue")
	public void leavingMethod(JoinPoint joinPoint, Loggable loggable, Object returnValue) {
		Signature signature = joinPoint.getSignature();
		String className = signature.getDeclaringType().getSimpleName();
		String methodName = signature.getName();
		logger.debug("leavingMethod {}::{}", className, methodName);
	}

	@AfterThrowing(pointcut = "logging(loggable)", throwing = "throwable", argNames = "joinPoint,loggable,throwable")
	public void leavingMethodException(JoinPoint joinPoint, Loggable loggable, Throwable throwable) {
		Signature signature = joinPoint.getSignature();
		String className = signature.getDeclaringType().getSimpleName();
		String methodName = signature.getName();
		String exceptionMessage = throwable.getMessage();
		logger.debug("leavingMethodException {}::{}. Reason: {}",
				new Object[] { className, methodName, exceptionMessage });
	}

	private static Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
}
