package org.aocdev.jdocusaurus.annotations.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Container annotation for repeatable {@link JDocResponse} annotations.
 *
 * <p>This annotation is automatically used by the compiler when multiple
 * {@code @JDocResponse} annotations are placed on the same method.
 * It should not be used directly.
 *
 * @see JDocResponse
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface JDocResponses {
    /** The array of {@link JDocResponse} annotations. */
    JDocResponse[] value();
}
