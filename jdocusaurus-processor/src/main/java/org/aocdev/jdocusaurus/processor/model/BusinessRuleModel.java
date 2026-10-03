/*
 * Copyright 2026 aocdev (Albert Ortells)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
