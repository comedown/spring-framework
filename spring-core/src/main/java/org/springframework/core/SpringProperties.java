/*
 * Copyright 2002-2016 the original author or authors.
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

package org.springframework.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Static holder for local Spring properties, i.e. defined at the Spring library level.
 *
 * <p>Reads a {@code spring.properties} file from the root of the Spring library classpath,
 * and also allows for programmatically setting properties through {@link #setProperty}.
 * When checking a property, local entries are being checked first, then falling back
 * to JVM-level system properties through a {@link System#getProperty} check.
 *
 * <p>This is an alternative way to set Spring-related system properties such as
 * "spring.getenv.ignore" and "spring.beaninfo.ignore", in particular for scenarios
 * where JVM system properties are locked on the target platform (e.g. WebSphere).
 * See {@link #setFlag} for a convenient way to locally set such flags to "true".
 *
 * <br><br>
 * 拉丁文缩写：
 * <ul>
 *     <li>i.e. ：即；也就是说</li>
 *     <li>e.g. ：举个例子</li>
 *     <li>etc. ：等等</li>
 * </ul>
 *
 * 本地Spring属性的静态持有类，也就是说，定义在Spring本地库级别的属性。
 *
 * <p>从Spring库的类路径根目录读取 {@code spring.properties} 文件，也允许通过 {@link #setProperty} 方法
 * 编程式的设置属性。在检查一个属性时，首先检查本地条目，然后返回到JVM层级通过{@link System#getProperty}检查
 * JVM系统属性。
 *
 * <p>这是设置Spring相关的系统属性比如 "spring.getenv.ignore"、"spring.beaninfo.ignore" 的另一种方式，
 * 尤其是在JVM系属性被目标平台锁定的情况（比如WebSphere）。请参阅 {@link #setFlag}，以获取一个方便的方式将
 * 这些本地化标记设置为"true"。
 *
 * @author Juergen Hoeller
 * @since 3.2.7
 * @see org.springframework.core.env.AbstractEnvironment#IGNORE_GETENV_PROPERTY_NAME
 * @see org.springframework.beans.CachedIntrospectionResults#IGNORE_BEANINFO_PROPERTY_NAME
 * @see org.springframework.jdbc.core.StatementCreatorUtils#IGNORE_GETPARAMETERTYPE_PROPERTY_NAME
 * @see org.springframework.test.context.cache.ContextCache#MAX_CONTEXT_CACHE_SIZE_PROPERTY_NAME
 */
public abstract class SpringProperties {

	private static final String PROPERTIES_RESOURCE_LOCATION = "spring.properties";

	private static final Log logger = LogFactory.getLog(SpringProperties.class);

	private static final Properties localProperties = new Properties();


	static {
		try {
			ClassLoader cl = SpringProperties.class.getClassLoader();
			URL url = (cl != null ? cl.getResource(PROPERTIES_RESOURCE_LOCATION) :
					ClassLoader.getSystemResource(PROPERTIES_RESOURCE_LOCATION));
			if (url != null) {
				logger.info("Found 'spring.properties' file in local classpath");
				InputStream is = url.openStream();
				try {
					localProperties.load(is);
				}
				finally {
					is.close();
				}
			}
		}
		catch (IOException ex) {
			if (logger.isInfoEnabled()) {
				logger.info("Could not load 'spring.properties' file from local classpath: " + ex);
			}
		}
	}


	/**
	 * Programmatically set a local property, overriding an entry in the
	 * {@code spring.properties} file (if any).
	 * @param key the property key
	 * @param value the associated property value, or {@code null} to reset it
	 */
	public static void setProperty(String key, String value) {
		if (value != null) {
			localProperties.setProperty(key, value);
		}
		else {
			localProperties.remove(key);
		}
	}

	/**
	 * Retrieve the property value for the given key, checking local Spring
	 * properties first and falling back to JVM-level system properties.
	 * @param key the property key
	 * @return the associated property value, or {@code null} if none found
	 */
	public static String getProperty(String key) {
		String value = localProperties.getProperty(key);
		if (value == null) {
			try {
				value = System.getProperty(key);
			}
			catch (Throwable ex) {
				if (logger.isDebugEnabled()) {
					logger.debug("Could not retrieve system property '" + key + "': " + ex);
				}
			}
		}
		return value;
	}

	/**
	 * Programmatically set a local flag to "true", overriding an
	 * entry in the {@code spring.properties} file (if any).
	 * @param key the property key
	 */
	public static void setFlag(String key) {
		localProperties.put(key, Boolean.TRUE.toString());
	}

	/**
	 * Retrieve the flag for the given property key.
	 * @param key the property key
	 * @return {@code true} if the property is set to "true",
	 * {@code} false otherwise
	 */
	public static boolean getFlag(String key) {
		return Boolean.parseBoolean(getProperty(key));
	}

}
