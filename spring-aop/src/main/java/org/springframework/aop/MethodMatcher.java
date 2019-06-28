/*
 * Copyright 2002-2018 the original author or authors.
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

package org.springframework.aop;

import java.lang.reflect.Method;

/**
 * Part of a {@link Pointcut}: Checks whether the target method is eligible for advice.
 *
 * <p>A MethodMatcher may be evaluated <b>statically</b> or at <b>runtime</b> (dynamically).
 * Static matching involves method and (possibly) method attributes. Dynamic matching
 * also makes arguments for a particular call available, and any effects of running
 * previous advice applying to the joinpoint.
 *
 * <p>If an implementation returns {@code false} from its {@link #isRuntime()}
 * method, evaluation can be performed statically, and the result will be the same
 * for all invocations of this method, whatever their arguments. This means that
 * if the {@link #isRuntime()} method returns {@code false}, the 3-arg
 * {@link #matches(java.lang.reflect.Method, Class, Object[])} method will never be invoked.
 *
 * <p>If an implementation returns {@code true} from its 2-arg
 * {@link #matches(java.lang.reflect.Method, Class)} method and its {@link #isRuntime()} method
 * returns {@code true}, the 3-arg {@link #matches(java.lang.reflect.Method, Class, Object[])}
 * method will be invoked <i>immediately before each potential execution of the related advice</i>,
 * to decide whether the advice should run. All previous advice, such as earlier interceptors
 * in an interceptor chain, will have run, so any state changes they have produced in
 * parameters or ThreadLocal state will be available at the time of evaluation.
 *
 * <br><br>
 * {@link Pointcut}的一部分：校验目标方法是否具有被通知资格。
 *
 * <p>一个MethodMatcher可以<b>静态地</b>或者运行时（动态的）评估。静态匹配涉及方法和（可能）方法参数。
 * 动态匹配也可以让参数对一次特殊调用可用，以及运行前通知的任何影响应用到切点上。
 *
 * <p>如果一个实现类的{@link #isRuntime()}方法返回{@code false}，可以使用静态评估，并且所有这个方法
 * 的调用都返回同样的结果，无论什么参数。这意味着如果{@link #isRuntime()}方法返回{@code false}，3个
 * 参数的{@link #matches(java.lang.reflect.Method, Class, Object[])}方法永远不会被调用。
 *
 * <p>如果一个实现类2个参数的{@link #matches(java.lang.reflect.Method, Class)}方法返回
 * {@code true}以及{@link #isRuntime()}返回{@code true}，那么<i>在相关通知的每个潜在的执行之前</i>
 * 3个参数的{@link #matches(java.lang.reflect.Method, Class, Object[])}方法将会立即执行，
 * 来决定通知是否会运行。所有前置通知，比如拦截器链中的提前拦截器，将会运行，因此所有他们参数中或ThreadLocal
 * 状态执行的状态改变将在评估是可获得。
 *
 * @author Rod Johnson
 * @since 11.11.2003
 * @see Pointcut
 * @see ClassFilter
 */
public interface MethodMatcher {

	/**
	 * Perform static checking whether the given method matches.
	 * <p>If this returns {@code false} or if the {@link #isRuntime()}
	 * method returns {@code false}, no runtime check (i.e. no
	 * {@link #matches(java.lang.reflect.Method, Class, Object[])} call)
	 * will be made.
	 * @param method the candidate method
	 * @param targetClass the target class (may be {@code null}, in which case
	 * the candidate class must be taken to be the method's declaring class)
	 * @return whether or not this method matches statically
	 */
	boolean matches(Method method, Class<?> targetClass);

	/**
	 * Is this MethodMatcher dynamic, that is, must a final call be made on the
	 * {@link #matches(java.lang.reflect.Method, Class, Object[])} method at
	 * runtime even if the 2-arg matches method returns {@code true}?
	 * <p>Can be invoked when an AOP proxy is created, and need not be invoked
	 * again before each method invocation,
	 * @return whether or not a runtime match via the 3-arg
	 * {@link #matches(java.lang.reflect.Method, Class, Object[])} method
	 * is required if static matching passed
	 */
	boolean isRuntime();

	/**
	 * Check whether there a runtime (dynamic) match for this method,
	 * which must have matched statically.
	 * <p>This method is invoked only if the 2-arg matches method returns
	 * {@code true} for the given method and target class, and if the
	 * {@link #isRuntime()} method returns {@code true}. Invoked
	 * immediately before potential running of the advice, after any
	 * advice earlier in the advice chain has run.
	 * @param method the candidate method
	 * @param targetClass the target class (may be {@code null}, in which case
	 * the candidate class must be taken to be the method's declaring class)
	 * @param args arguments to the method
	 * @return whether there's a runtime match
	 * @see MethodMatcher#matches(Method, Class)
	 */
	boolean matches(Method method, Class<?> targetClass, Object... args);


	/**
	 * Canonical instance that matches all methods.
	 *
	 * <br><br>
	 * 简单的实例，匹配所有方法。
	 */
	MethodMatcher TRUE = TrueMethodMatcher.INSTANCE;

}
