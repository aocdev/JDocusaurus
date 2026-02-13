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
