package org.aocdev.jdocusaurus.annotations.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documents an API controller or service class.
 *
 * <p>Place this annotation on a class to generate a dedicated Markdown page
 * under the {@code api/} directory with endpoint tables, request/response
 * details, and optional Mermaid sequence diagrams.
 *
 * <p><b>Example:</b>
 * <pre>{@code
 * @JDocClass(
 *     name = "User Controller",
 *     description = "User management REST API",
 *     basePath = "/api/v1/users",
 *     version = "v1"
 * )
 * public class UserController { }
 * }</pre>
 *
 * @see JDocEndpoint
 * @see JDocParam
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface JDocClass {
    /** Display name for the class in generated docs. Defaults to the simple class name. */
    String name() default "";
    /** Description of the class purpose. */
    String description();
    /** Base URL path for all endpoints in this class (e.g., {@code "/api/v1/users"}). */
    String basePath() default "";
    /** API version identifier (e.g., {@code "v1"}). */
    String version() default "";
    /** Logical group for sidebar organization. */
    String group() default "";
    /** Tags for categorization and filtering. */
    String[] tags() default {};
}
