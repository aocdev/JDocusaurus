package org.aocdev.jdocusaurus.annotations.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Container annotation for repeatable {@link JDocProduces} annotations.
 *
 * <p>Automatically used by the compiler. Should not be used directly.
 *
 * @see JDocProduces
 * @since 1.0.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface JDocProducesAll {
    /** The array of {@link JDocProduces} annotations. */
    JDocProduces[] value();
}
