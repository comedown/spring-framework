/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.aop.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Test;

import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.tests.sample.beans.ITestBean;
import org.springframework.tests.sample.beans.TestBean;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Test for correct application of the bean() PCD for &#64;AspectJ-based aspects.
 *
 * @author Ramnivas Laddad
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public class BeanNamePointcutAtAspectTests {

	private ITestBean testBean1;

	private ITestBean testBean3;

	private CounterAspect counterAspect;


	@org.junit.Before
	@SuppressWarnings("resource")
	public void setUp() {
		ClassPathXmlApplicationContext ctx =
				new ClassPathXmlApplicationContext(getClass().getSimpleName() + ".xml", getClass());

		counterAspect = (CounterAspect) ctx.getBean("counterAspect");
		testBean1 = (ITestBean) ctx.getBean("testBean1");
		testBean3 = (ITestBean) ctx.getBean("testBean3");
	}

	@Test
	public void testMatchingBeanName() {
		assertTrue("Expected a proxy", testBean1 instanceof Advised);

		// Call two methods to test for SPR-3953-like condition
		testBean1.setAge(20);
		testBean1.setName("");
		assertEquals(2, counterAspect.count);
	}

	@Test
	public void testNonMatchingBeanName() {
		assertFalse("Didn't expect a proxy", testBean3 instanceof Advised);

		testBean3.setAge(20);
		assertEquals(0, counterAspect.count);
	}

	@Test
	public void testProgrammaticProxyCreation() {
		ITestBean testBean = new TestBean();

		AspectJProxyFactory factory = new AspectJProxyFactory();
		factory.setTarget(testBean);

		CounterAspect myCounterAspect = new CounterAspect();
		factory.addAspect(myCounterAspect);

		ITestBean proxyTestBean = factory.getProxy();

		assertTrue("Expected a proxy", proxyTestBean instanceof Advised);
		proxyTestBean.setAge(20);
		assertEquals("Programmatically created proxy shouldn't match bean()", 0, myCounterAspect.count);
	}

}


@Aspect
class CounterAspect {

	int count;

	Map<String, Method> lruMap = new LinkedHashMap<>(2);

	@Pointcut("execution(* set*(..)) && bean(testBean1)")
	public void ponitCut() {
	}

//	@Before("execution(* set*(..)) && bean(testBean1)")
	@Before("ponitCut()")
	public void increment1ForAnonymousPointcut(JoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		System.out.println(signature.getName());
		count++;
	}

//	@After("execution(* set*(..)) && bean(testBean1)")
	@After("ponitCut()")
	public void leastRecentAccessLog(JoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		System.out.println(signature.getName());
//		lruMap.put();
	}

	@Around("ponitCut()")
	public void aroundLog(ProceedingJoinPoint point) {
		MethodSignature methodSignature = (MethodSignature) point.getSignature();
		Method method = methodSignature.getMethod();
		System.out.println("around before : " + method.getName());
		try {
			point.proceed();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		System.out.println("around after : " + method.getName());
	}

}
