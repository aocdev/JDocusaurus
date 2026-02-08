package org.aocdev.jdocusaurus.annotations.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface JDocClass {
    String name() default "";
    String description();
    String basePath() default "";
    String version() default "";
    String group() default "";
    String[] tags() default {};
}
