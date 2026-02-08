package org.aocdev.jdocusaurus.annotations.config;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
@Repeatable(JDocConfigs.class)
public @interface JDocConfig {
    String key();
    String description() default "";
    String defaultValue() default "";
    boolean required() default false;
    boolean secret() default false;
    String example() default "";
}
