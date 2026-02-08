package org.aocdev.jdocusaurus.annotations.event;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
@Repeatable(JDocConsumesAll.class)
public @interface JDocConsumes {
    Class<?> event() default Void.class;
    String topic() default "";
    String description() default "";
    String group() default "";
}
