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

package org.springframework.core;

/**
 * {@code Ordered} is an interface that can be implemented by objects that
 * should be <em>orderable</em>, for example in a {@code Collection}.
 *
 * <p>The actual {@link #getOrder() order} can be interpreted as prioritization,
 * with the first object (with the lowest order value) having the highest
 * priority.
 *
 * <p>Note that there is also a <em>priority</em> marker for this interface:
 * {@link PriorityOrdered}. Order values expressed by {@code PriorityOrdered}
 * objects always apply before same order values expressed by <em>plain</em>
 * {@link Ordered} objects.
 *
 * <p>Consult the Javadoc for {@link OrderComparator} for details on the
 * sort semantics for non-ordered objects.
 *
 * <br><br>
 * {@code Ordered}接口用于被需要<em>排序</em>的对象实现，比如在{@code Collection}集合中。
 *
 * <p>实际的{@link #getOrder() order}顺序可以解释为优先级排序，第一个对象（顺序值最小）的优先级最高。
 *
 * <p>注意：还有一个<em>优先级</em>标记接口：{@link PriorityOrdered}。由{@code PriorityOrdered}
 * 对象表示的顺序值始终优先于由<em>普通的</em>{@link Ordered}对象表示的相同顺序值。
 *
 * <p>有关非排序对象的排序语义的详细信息，请参阅{@link OrderComparator} Javadoc。
 *
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since 07.04.2003
 * @see PriorityOrdered
 * @see OrderComparator
 * @see org.springframework.core.annotation.Order
 * @see org.springframework.core.annotation.AnnotationAwareOrderComparator
 */
public interface Ordered {

	/**
	 * Useful constant for the highest precedence value.
	 * @see java.lang.Integer#MIN_VALUE
	 */
	int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

	/**
	 * Useful constant for the lowest precedence value.
	 * @see java.lang.Integer#MAX_VALUE
	 */
	int LOWEST_PRECEDENCE = Integer.MAX_VALUE;


	/**
	 * Get the order value of this object.
	 * <p>Higher values are interpreted as lower priority. As a consequence,
	 * the object with the lowest value has the highest priority (somewhat
	 * analogous to Servlet {@code load-on-startup} values).
	 * <p>Same order values will result in arbitrary sort positions for the
	 * affected objects.
	 * @return the order value
	 * @see #HIGHEST_PRECEDENCE
	 * @see #LOWEST_PRECEDENCE
	 */
	int getOrder();

}
