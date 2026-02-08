package org.aocdev.jdocusaurus.annotations.api;

import org.aocdev.jdocusaurus.annotations.enums.ParamLocation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.SOURCE)
public @interface JDocParam {
    String name() default "";
    String description();
    ParamLocation location();
    boolean required() default true;
    String defaultValue() default "";
    String example() default "";
    String type() default "";
}
