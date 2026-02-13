package org.aocdev.jdocusaurus.annotations.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Container annotation for repeatable {@link JDocConfig} annotations.
 *
 * <p>Automatically used by the compiler. Should not be used directly.
 *
 * @see JDocConfig
 * @since 1.0.0
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface JDocConfigs {
    /** The array of {@link JDocConfig} annotations. */
    JDocConfig[] value();
}
