package org.aocdev.jdocusaurus.annotations.data;

import org.aocdev.jdocusaurus.annotations.enums.RelationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface JDocRelation {
    String target() default "";
    RelationType type();
    String description() default "";
}
