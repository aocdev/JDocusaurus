package org.aocdev.jdocusaurus.annotations.api;

import org.aocdev.jdocusaurus.annotations.enums.ParamLocation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documents a parameter of a {@link JDocEndpoint}-annotated method.
 *
 * <p>Generates a parameter table row with name, location, type,
 * and validation constraints in the endpoint documentation.
 *
 * <p><b>Example:</b>
 * <pre>{@code
 * public Object getUser(
 *     @JDocParam(name = "id", description = "User ID",
 *                location = ParamLocation.PATH, example = "123")
 *     Long id
 * ) { }
 * }</pre>
 *
 * @see JDocEndpoint
 * @see ParamLocation
 * @since 1.0.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.SOURCE)
public @interface JDocParam {
    /** Parameter name. Defaults to the Java parameter name. */
    String name() default "";
    /** Description of the parameter purpose. */
    String description();
    /** Where the parameter is located in the HTTP request. */
    ParamLocation location();
    /** Whether the parameter is mandatory. */
    boolean required() default true;
    /** Default value when the parameter is omitted. */
    String defaultValue() default "";
    /** Example value for documentation. */
    String example() default "";
    /** Type override (e.g., {@code "UUID"}). Defaults to the Java type. */
    String type() default "";
}
