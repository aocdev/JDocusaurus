package org.aocdev.jdocusaurus.annotations.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documents an HTTP response for a {@link JDocEndpoint}-annotated method.
 *
 * <p>This annotation is {@link Repeatable}, so multiple responses can be
 * declared on the same method. Generates a response table with status codes,
 * descriptions, and optional body type/example.
 *
 * <p><b>Example:</b>
 * <pre>{@code
 * @JDocResponse(code = 200, description = "User found", type = UserDTO.class)
 * @JDocResponse(code = 404, description = "User not found")
 * public Object getUserById(Long id) { }
 * }</pre>
 *
 * <p><b>Note:</b> The {@link #type()} attribute is read at compile time via
 * the {@code MirroredTypeException} pattern since {@code Class<?>} values
 * are not directly accessible during annotation processing.
 *
 * @see JDocResponses
 * @see JDocEndpoint
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@Repeatable(JDocResponses.class)
public @interface JDocResponse {
    /** HTTP status code (e.g., 200, 404, 500). */
    int code();
    /** Description of when this response is returned. */
    String description();
    /** Response body type. Read via {@code MirroredTypeException} at compile time. */
    Class<?> type() default Void.class;
    /** JSON example of the response body. */
    String example() default "";
}
