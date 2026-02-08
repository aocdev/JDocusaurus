package org.aocdev.jdocusaurus.annotations.integration;

import org.aocdev.jdocusaurus.annotations.enums.ServiceType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
public @interface JDocExternalService {
    String name();
    String description() default "";
    String url() default "";
    ServiceType type() default ServiceType.REST;
    String owner() default "";
}
