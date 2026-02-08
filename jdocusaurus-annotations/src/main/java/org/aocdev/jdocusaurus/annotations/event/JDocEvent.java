package org.aocdev.jdocusaurus.annotations.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface JDocEvent {
    String name();
    String description() default "";
    String topic() default "";
    String schema() default "";
}
