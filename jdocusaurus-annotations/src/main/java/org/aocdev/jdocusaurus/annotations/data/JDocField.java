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
package org.aocdev.jdocusaurus.annotations.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documents a field within a {@link JDocEntity}-annotated class.
 *
 * <p>Generates a row in the entity field table with type, nullability,
 * constraints, and example values. JPA {@code @Column} metadata is used
 * as fallback when attributes are not explicitly set.
 *
 * <p><b>Example:</b>
 * <pre>{@code
 * @JDocField(description = "User email", example = "john@example.com",
 *            nullable = false, constraints = "UNIQUE")
 * private String email;
 * }</pre>
 *
 * @see JDocEntity
 * @since 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface JDocField {
    /** Description of the field purpose. */
    String description() default "";
    /** Example value for documentation. */
    String example() default "";
    /** Whether this field allows null values. */
    boolean nullable() default true;
    /** Database constraints (e.g., {@code "PK, AUTO_INCREMENT"}, {@code "UNIQUE"}). */
    String constraints() default "";
}
