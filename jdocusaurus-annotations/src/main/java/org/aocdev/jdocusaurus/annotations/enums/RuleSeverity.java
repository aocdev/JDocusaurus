package org.aocdev.jdocusaurus.annotations.enums;

/**
 * Severity level of a business rule, for use with
 * {@link org.aocdev.jdocusaurus.annotations.rule.JDocBusinessRule#severity()}.
 *
 * <p>Rules are grouped by severity in the generated documentation.
 *
 * @since 1.0.0
 */
public enum RuleSeverity {
    /** Must be enforced; violation causes a hard error. */
    MANDATORY,
    /** Should be followed; violation triggers a warning. */
    WARNING,
    /** Informational guideline. */
    INFO
}
