package org.aocdev.jdocusaurus.processor.generator;

import org.aocdev.jdocusaurus.processor.model.BusinessRuleModel;
import org.aocdev.jdocusaurus.processor.model.ProjectModel;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Generates business rules documentation grouped by severity.
 *
 * <p>Output: {@code business-rules/index.md} with a traceability table
 * showing rule IDs, descriptions, locations, and cross-references.
 *
 * @since 1.0.0
 */
public class RuleMarkdownGenerator implements Generator {

    @Override
    public void generate(ProjectModel model, DocWriter writer) throws IOException {
        List<BusinessRuleModel> rules = model.getBusinessRules();
        if (rules.isEmpty()) return;

        writer.write("business-rules/index.md", generateRulesIndex(rules));
    }

    private String generateRulesIndex(List<BusinessRuleModel> rules) {
        StringBuilder md = new StringBuilder();

        md.append("---\n");
        md.append("sidebar_label: \"Reglas de Negocio\"\n");
        md.append("---\n\n");

        md.append("# Reglas de Negocio\n\n");

        Map<String, List<BusinessRuleModel>> bySeverity = rules.stream()
                .collect(Collectors.groupingBy(BusinessRuleModel::getSeverity, LinkedHashMap::new, Collectors.toList()));

        List<String> severityOrder = List.of("MANDATORY", "WARNING", "INFO");
        for (String severity : severityOrder) {
            List<BusinessRuleModel> group = bySeverity.get(severity);
            if (group == null || group.isEmpty()) continue;

            String badge = switch (severity) {
                case "MANDATORY" -> "OBLIGATORIA";
                case "WARNING" -> "ADVERTENCIA";
                case "INFO" -> "INFORMATIVA";
                default -> severity;
            };

            md.append("## ").append(badge).append("\n\n");
            md.append("| ID | Regla | Aplicada en | Reglas relacionadas |\n");
            md.append("|----|-------|------------|--------------------|\n");

            for (BusinessRuleModel rule : group) {
                md.append("| `").append(rule.getId()).append("` | ");
                md.append(rule.getRule()).append(" | ");
                md.append("`").append(rule.getLocation()).append("` | ");
                if (rule.getRelatedRules() != null && rule.getRelatedRules().length > 0) {
                    md.append(String.join(", ", rule.getRelatedRules()));
                } else {
                    md.append("-");
                }
                md.append(" |\n");
            }
            md.append("\n");
        }

        return md.toString();
    }
}
