package org.aocdev.jdocusaurus.annotations.rule;

import org.aocdev.jdocusaurus.annotations.enums.RuleSeverity;

import java.lang.annotation.*;

/**
 * Documents a business rule enforced by a class or method.
 *
 * <p>This annotation is {@link Repeatable}. Rules are collected and grouped
 * by {@link RuleSeverity} in the generated {@code business-rules/index.md}
 * page with a traceability table.
 *
 * <p><b>Example:</b>
 * <pre>{@code
 * @JDocBusinessRule(id = "BR-001", rule = "Email must be unique",
 *                   severity = RuleSeverity.MANDATORY)
 * @JDocBusinessRule(id = "BR-002", rule = "Password >= 8 chars",
 *                   severity = RuleSeverity.MANDATORY, relatedRules = {"BR-001"})
 * public Object create(Object user) { }
 * }</pre>
 *
 * @see JDocBusinessRules
 * @see RuleSeverity
 * @since 1.0.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
@Repeatable(JDocBusinessRules.class)
public @interface JDocBusinessRule {
    /** Unique rule identifier (e.g., {@code "BR-001"}). */
    String id();
    /** Human-readable rule description. */
    String rule();
    /** Severity level that determines grouping in the output. */
    RuleSeverity severity() default RuleSeverity.MANDATORY;
    /** IDs of related rules for cross-referencing. */
    String[] relatedRules() default {};
}
