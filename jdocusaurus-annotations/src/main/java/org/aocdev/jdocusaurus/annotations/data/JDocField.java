package org.aocdev.jdocusaurus.annotations.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface JDocField {
    String description() default "";
    String example() default "";
    boolean nullable() default true;
    String constraints() default "";
}
