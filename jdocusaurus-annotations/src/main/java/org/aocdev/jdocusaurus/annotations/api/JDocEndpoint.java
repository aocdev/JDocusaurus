package org.aocdev.jdocusaurus.annotations.api;

import org.aocdev.jdocusaurus.annotations.enums.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documents an HTTP endpoint within a {@link JDocClass}-annotated controller.
 *
 * <p>Generates a row in the endpoint table with method, path, description,
 * content types, and authentication details. Combine with {@link JDocResponse}
 * and {@link JDocParam} for complete endpoint documentation.
 *
 * <p><b>Example:</b>
 * <pre>{@code
 * @JDocEndpoint(
 *     method = HttpMethod.GET,
 *     path = "/{id}",
 *     description = "Gets a user by ID",
 *     summary = "Get user"
 * )
 * public Object getUserById(Long id) { }
 * }</pre>
 *
 * @see JDocClass
 * @see JDocResponse
 * @see JDocParam
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface JDocEndpoint {
    /** HTTP method (GET, POST, PUT, DELETE, etc.). */
    HttpMethod method();
    /** URL path relative to the class {@link JDocClass#basePath()}. */
    String path();
    /** Detailed description of what the endpoint does. */
    String description();
    /** Short one-line summary for tables and indexes. */
    String summary() default "";
    /** Response content type. */
    String produces() default "application/json";
    /** Request content type. */
    String consumes() default "application/json";
    /** Whether this endpoint is deprecated. */
    boolean deprecated() default false;
    /** Deprecation message explaining the alternative. */
    String deprecatedMessage() default "";
    /** Authentication requirement (e.g., {@code "Bearer JWT"}). Empty means no auth required. */
    String auth() default "";
}
