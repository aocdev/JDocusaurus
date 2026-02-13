package org.aocdev.jdocusaurus.annotations.rule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Container annotation for repeatable {@link JDocBusinessRule} annotations.
 *
 * <p>Automatically used by the compiler. Should not be used directly.
 *
 * @see JDocBusinessRule
 * @since 1.0.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface JDocBusinessRules {
    /** The array of {@link JDocBusinessRule} annotations. */
    JDocBusinessRule[] value();
}
