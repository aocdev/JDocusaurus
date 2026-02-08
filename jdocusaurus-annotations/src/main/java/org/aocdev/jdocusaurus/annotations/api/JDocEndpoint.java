package org.aocdev.jdocusaurus.annotations.api;

import org.aocdev.jdocusaurus.annotations.enums.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface JDocEndpoint {
    HttpMethod method();
    String path();
    String description();
    String summary() default "";
    String produces() default "application/json";
    String consumes() default "application/json";
    boolean deprecated() default false;
    String deprecatedMessage() default "";
    String auth() default "";
}
