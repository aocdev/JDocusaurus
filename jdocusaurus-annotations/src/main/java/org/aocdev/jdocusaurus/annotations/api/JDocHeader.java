/*
 * Copyright 2026 aocdev (Albert Ortells)
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
package org.aocdev.jdocusaurus.annotations.api;

import org.aocdev.jdocusaurus.annotations.enums.HeaderDirection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documents an HTTP header for a {@link JDocEndpoint}-annotated method.
 *
 * <p>This annotation is {@link Repeatable}. It generates a header table
 * in the endpoint documentation showing name, direction, and requirement.
 *
 * <p><b>Example:</b>
 * <pre>{@code
 * @JDocHeader(name = "Authorization", description = "Bearer JWT token")
 * @JDocHeader(name = "X-Request-Id", description = "Trace ID", required = false)
 * public Object createUser(Object user) { }
 * }</pre>
 *
 * @see JDocHeaders
 * @see HeaderDirection
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@Repeatable(JDocHeaders.class)
public @interface JDocHeader {
    /** Header name (e.g., {@code "Authorization"}, {@code "Content-Type"}). */
    String name();
    /** Description of the header purpose. */
    String description();
    /** Whether the header is mandatory. */
    boolean required() default true;
    /** Whether this is a request header, response header, or both. */
    HeaderDirection direction() default HeaderDirection.REQUEST;
}
