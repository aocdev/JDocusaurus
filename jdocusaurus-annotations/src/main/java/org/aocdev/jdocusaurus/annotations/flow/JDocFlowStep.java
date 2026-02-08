package org.aocdev.jdocusaurus.annotations.flow;

import org.aocdev.jdocusaurus.annotations.enums.StepType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@Repeatable(JDocFlowSteps.class)
public @interface JDocFlowStep {
    String flow();
    int order() default 0;
    String from();
    String to();
    String message();
    StepType type() default StepType.SYNC;
    String returnMessage() default "";
    String condition() default "";
    String note() default "";
}
