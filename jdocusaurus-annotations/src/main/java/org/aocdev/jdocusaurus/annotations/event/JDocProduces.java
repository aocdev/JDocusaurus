package org.aocdev.jdocusaurus.annotations.event;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
@Repeatable(JDocProducesAll.class)
public @interface JDocProduces {
    Class<?> event() default Void.class;
    String topic() default "";
    String description() default "";
    boolean async() default true;
}
