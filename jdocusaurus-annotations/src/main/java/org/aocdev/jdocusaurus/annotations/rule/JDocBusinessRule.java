package org.aocdev.jdocusaurus.annotations.rule;

import org.aocdev.jdocusaurus.annotations.enums.RuleSeverity;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
@Repeatable(JDocBusinessRules.class)
public @interface JDocBusinessRule {
    String id();
    String rule();
    RuleSeverity severity() default RuleSeverity.MANDATORY;
    String[] relatedRules() default {};
}
