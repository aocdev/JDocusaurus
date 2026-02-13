package org.aocdev.jdocusaurus.annotations.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Container annotation for repeatable {@link JDocHeader} annotations.
 *
 * <p>Automatically used by the compiler when multiple {@code @JDocHeader}
 * annotations are placed on the same method. Should not be used directly.
 *
 * @see JDocHeader
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface JDocHeaders {
    /** The array of {@link JDocHeader} annotations. */
    JDocHeader[] value();
}
