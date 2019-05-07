/*
 * Copyright 2002-2012 the original author or authors.
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

package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

/**
 * Allows for custom modification of an application context's bean definitions,
 * adapting the bean property values of the context's underlying bean factory.
 *
 * <p>Application contexts can auto-detect BeanFactoryPostProcessor beans in
 * their bean definitions and apply them before any other beans get created.
 *
 * <p>Useful for custom config files targeted at system administrators that
 * override bean properties configured in the application context.
 *
 * <p>See PropertyResourceConfigurer and its concrete implementations
 * for out-of-the-box solutions that address such configuration needs.
 *
 * <p>A BeanFactoryPostProcessor may interact with and modify bean
 * definitions, but never bean instances. Doing so may cause premature bean
 * instantiation, violating the container and causing unintended side-effects.
 * If bean instance interaction is required, consider implementing
 * {@link BeanPostProcessor} instead.
 *
 * <br><br>
 * 允许自定义修改应用程序上下文的bean定义，调整上下文基础bean工厂的bean属性值。
 *
 * <p>应用程序上下文能够自动检测到它的bean定义中BeanFactoryPostProcessor的bean，
 * 并且在创建其他bean之前应用他们。
 *
 * <p>对于以系统管理员为目标的自定义配置文件很有用，这些文件覆盖应用程序上下文中配置的bean属性。
 *
 * <p>有关解决此类配置需求的现成解决方案，请参阅PropertyResourceConfigurer及其具体实现。
 *
 * <p>BeanFactoryPostProcessor可以与bean定义交互和修改bean定义，但不能修改bean实例。
 * 这样做可能导致过早的bean实例化，违反容器规则并导致意外的副作用。如果需要bean实例交互，
 * 请考虑实现{@link BeanPostProcessor}。
 *
 * @author Juergen Hoeller
 * @since 06.07.2003
 * @see BeanPostProcessor
 * @see PropertyResourceConfigurer
 */
public interface BeanFactoryPostProcessor {

	/**
	 * Modify the application context's internal bean factory after its standard
	 * initialization. All bean definitions will have been loaded, but no beans
	 * will have been instantiated yet. This allows for overriding or adding
	 * properties even to eager-initializing beans.
	 *
	 * <br><br>
	 * 在应用程序上下文的标准初始化之后修改其内部bean工厂。所有bean定义都已经被加载，但还没有实例化bean。
	 * 这允许覆盖或添加bean属性，甚至是早就初始化的bean。
	 *
	 * @param beanFactory the bean factory used by the application context
	 * @throws org.springframework.beans.BeansException in case of errors
	 */
	void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;

}
