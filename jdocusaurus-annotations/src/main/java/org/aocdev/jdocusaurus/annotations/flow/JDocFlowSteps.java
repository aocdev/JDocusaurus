package org.aocdev.jdocusaurus.annotations.flow;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Container annotation for repeatable {@link JDocFlowStep} annotations.
 *
 * <p>Automatically used by the compiler. Should not be used directly.
 *
 * @see JDocFlowStep
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface JDocFlowSteps {
    /** The array of {@link JDocFlowStep} annotations. */
    JDocFlowStep[] value();
}
