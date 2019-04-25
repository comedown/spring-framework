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

package org.springframework.context.config;

import org.w3c.dom.Element;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.util.StringUtils;

/**
 * Parser for the {@code <context:property-placeholder/>} element.
 *
 * <br><br>
 * {@code <context:property-placeholder/>}元素解析器。
 * 解析为{@link PropertyPlaceholderConfigurer}
 *
 * @author Juergen Hoeller
 * @author Dave Syer
 * @author Chris Beams
 * @since 2.5
 */
class PropertyPlaceholderBeanDefinitionParser extends AbstractPropertyLoadingBeanDefinitionParser {

	private static final String SYSTEM_PROPERTIES_MODE_ATTRIBUTE = "system-properties-mode";

	private static final String SYSTEM_PROPERTIES_MODE_DEFAULT = "ENVIRONMENT";


	@Override
	protected Class<?> getBeanClass(Element element) {
		// As of Spring 3.1, the default value of system-properties-mode has changed from
		// 'FALLBACK' to 'ENVIRONMENT'. This latter value indicates that resolution of
		// placeholders against system properties is a function of the Environment and
		// its current set of PropertySources.
		if (SYSTEM_PROPERTIES_MODE_DEFAULT.equals(element.getAttribute(SYSTEM_PROPERTIES_MODE_ATTRIBUTE))) {
			return PropertySourcesPlaceholderConfigurer.class;
		}

		// The user has explicitly specified a value for system-properties-mode: revert to
		// PropertyPlaceholderConfigurer to ensure backward compatibility with 3.0 and earlier.
		return PropertyPlaceholderConfigurer.class;
	}

	/**
	 * 解析context:property-placeholder元素
	 * @param element
	 * @param parserContext
	 * @param builder
	 */
	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		super.doParse(element, parserContext, builder);

		// 是否忽略不能解析的，默认false
		builder.addPropertyValue("ignoreUnresolvablePlaceholders",
				Boolean.valueOf(element.getAttribute("ignore-unresolvable")));

		// 默认：ENVIRONMENT
		String systemPropertiesModeName = element.getAttribute(SYSTEM_PROPERTIES_MODE_ATTRIBUTE);
		if (StringUtils.hasLength(systemPropertiesModeName) &&
				!systemPropertiesModeName.equals(SYSTEM_PROPERTIES_MODE_DEFAULT)) {
			builder.addPropertyValue("systemPropertiesModeName", "SYSTEM_PROPERTIES_MODE_" + systemPropertiesModeName);
		}

		if (element.hasAttribute("value-separator")) {
			builder.addPropertyValue("valueSeparator", element.getAttribute("value-separator"));
		}
		if (element.hasAttribute("trim-values")) {
			builder.addPropertyValue("trimValues", element.getAttribute("trim-values"));
		}
		if (element.hasAttribute("null-value")) {
			builder.addPropertyValue("nullValue", element.getAttribute("null-value"));
		}
	}

}
