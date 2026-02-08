package org.aocdev.jdocusaurus.annotations.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@Repeatable(JDocResponses.class)
public @interface JDocResponse {
    int code();
    String description();
    Class<?> type() default Void.class;
    String example() default "";
}
