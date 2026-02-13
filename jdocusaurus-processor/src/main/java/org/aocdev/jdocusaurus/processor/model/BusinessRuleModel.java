package org.aocdev.jdocusaurus.processor.model;

/**
 * Model representing a {@code @JDocBusinessRule}-annotated business rule.
 *
 * <p>Includes the rule ID, severity, description, and the location
 * (class/method) where it is enforced.
 *
 * @since 1.0.0
 */
public class BusinessRuleModel {
    private String id;
    private String rule;
    private String severity;
    private String[] relatedRules;
    private String appliedInClass;
    private String appliedInMethod;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRule() { return rule; }
    public void setRule(String rule) { this.rule = rule; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String[] getRelatedRules() { return relatedRules; }
    public void setRelatedRules(String[] relatedRules) { this.relatedRules = relatedRules; }

    public String getAppliedInClass() { return appliedInClass; }
    public void setAppliedInClass(String appliedInClass) { this.appliedInClass = appliedInClass; }

    public String getAppliedInMethod() { return appliedInMethod; }
    public void setAppliedInMethod(String appliedInMethod) { this.appliedInMethod = appliedInMethod; }

    public String getLocation() {
        if (appliedInMethod != null && !appliedInMethod.isEmpty()) {
            return appliedInClass + "." + appliedInMethod + "()";
        }
        return appliedInClass;
    }
}
