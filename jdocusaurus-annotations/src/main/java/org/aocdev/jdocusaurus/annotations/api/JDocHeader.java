package org.aocdev.jdocusaurus.annotations.api;

import org.aocdev.jdocusaurus.annotations.enums.HeaderDirection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@Repeatable(JDocHeaders.class)
public @interface JDocHeader {
    String name();
    String description();
    boolean required() default true;
    HeaderDirection direction() default HeaderDirection.REQUEST;
}
