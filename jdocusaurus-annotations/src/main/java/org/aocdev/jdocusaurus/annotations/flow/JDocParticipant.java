package org.aocdev.jdocusaurus.annotations.flow;

import org.aocdev.jdocusaurus.annotations.enums.ParticipantType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface JDocParticipant {
    String name();
    String alias() default "";
    ParticipantType type() default ParticipantType.SERVICE;
}
