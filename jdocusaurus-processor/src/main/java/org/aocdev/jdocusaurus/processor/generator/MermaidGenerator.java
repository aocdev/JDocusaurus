package org.aocdev.jdocusaurus.processor.generator;

import org.aocdev.jdocusaurus.processor.model.*;

import java.util.*;

/**
 * Utility class that generates Mermaid diagram markup from model data.
 *
 * <p>Produces:
 * <ul>
 *   <li>Sequence diagrams from {@link CallGraphModel} or manual flow steps</li>
 *   <li>ER diagrams (full and simplified) from entities and relations</li>
 *   <li>Event flow graphs showing producers and consumers</li>
 *   <li>Dependency maps for external service integrations</li>
 * </ul>
 *
 * @since 1.0.0
 */
public class MermaidGenerator {

    public String generateSequenceDiagramFromCallGraph(CallGraphModel graph, String httpMethod,
                                                        String path, List<ParticipantModel> knownParticipants) {
        if (graph == null || graph.isEmpty()) return null;

        StringBuilder md = new StringBuilder();
        md.append("```mermaid\nsequenceDiagram\n");

        Set<String> participants = new LinkedHashSet<>();
        participants.add("Client");
        participants.add(graph.getSourceClass());
        collectParticipants(graph, participants);

        Map<String, String> aliasMap = buildAliasMap(knownParticipants);
        for (String p : participants) {
            String alias = aliasMap.get(p);
            if (alias != null && !alias.equals(p)) {
                md.append("    participant ").append(p).append(" as ").append(alias).append("\n");
            } else {
                md.append("    participant ").append(p).append("\n");
            }
        }
        md.append("\n");

        md.append("    Client->>").append(graph.getSourceClass()).append(": ")
                .append(httpMethod).append(" ").append(path).append("\n");

        renderEdges(graph, md, new HashSet<>());

        md.append("    ").append(graph.getSourceClass()).append("-->>Client: response\n");

        md.append("```\n");
        return md.toString();
    }

    public String generateSequenceDiagramFromManualSteps(List<FlowStepModel> steps,
                                                          List<ParticipantModel> knownParticipants) {
        if (steps == null || steps.isEmpty()) return null;

        StringBuilder md = new StringBuilder();
        md.append("```mermaid\nsequenceDiagram\n");

        Set<String> participants = new LinkedHashSet<>();
        for (FlowStepModel step : steps) {
            participants.add(step.getFrom());
            participants.add(step.getTo());
        }

        Map<String, String> aliasMap = buildAliasMap(knownParticipants);
        for (String p : participants) {
            String alias = aliasMap.get(p);
            if (alias != null && !alias.equals(p)) {
                md.append("    participant ").append(p).append(" as ").append(alias).append("\n");
            } else {
                md.append("    participant ").append(p).append("\n");
            }
        }
        md.append("\n");

        String currentCondition = null;
        for (FlowStepModel step : steps) {
            if (!step.getCondition().isEmpty() && !step.getCondition().equals(currentCondition)) {
                if (currentCondition != null) {
                    md.append("    end\n");
                }
                md.append("    alt ").append(step.getCondition()).append("\n");
                currentCondition = step.getCondition();
            } else if (step.getCondition().isEmpty() && currentCondition != null) {
                md.append("    end\n");
                currentCondition = null;
            }

            String arrow = step.getType().equals("ASYNC") ? "-)>" : "->>";
            String indent = currentCondition != null ? "        " : "    ";
            md.append(indent).append(step.getFrom()).append(arrow)
                    .append(step.getTo()).append(": ").append(step.getMessage()).append("\n");

            if (!step.getReturnMessage().isEmpty()) {
                md.append(indent).append(step.getTo()).append("-->>")
                        .append(step.getFrom()).append(": ").append(step.getReturnMessage()).append("\n");
            }

            if (!step.getNote().isEmpty()) {
                md.append(indent).append("Note over ").append(step.getFrom()).append(",")
                        .append(step.getTo()).append(": ").append(step.getNote()).append("\n");
            }
        }

        if (currentCondition != null) {
            md.append("    end\n");
        }

        md.append("```\n");
        return md.toString();
    }

    private void renderEdges(CallGraphModel graph, StringBuilder md, Set<String> rendered) {
        String currentCondition = null;

        for (CallGraphModel.CallEdge edge : graph.getEdges()) {
            String edgeKey = edge.getSourceClass() + "->" + edge.getTargetFieldType() + "." + edge.getTargetMethod();
            if (rendered.contains(edgeKey)) continue;
            rendered.add(edgeKey);

            if (edge.getCondition() != null && !edge.getCondition().isEmpty()) {
                if (!edge.getCondition().equals(currentCondition)) {
                    if (currentCondition != null) md.append("    end\n");
                    if (edge.getCondition().equals("else")) {
                        md.append("    else\n");
                    } else {
                        md.append("    alt ").append(edge.getCondition()).append("\n");
                    }
                    currentCondition = edge.getCondition();
                }
            } else if (currentCondition != null) {
                md.append("    end\n");
                currentCondition = null;
            }

            String indent = currentCondition != null ? "        " : "    ";
            String arrow = edge.isAsync() ? "-)>" : "->>";
            String callLabel = edge.getTargetMethod() + "(" + edge.getArguments() + ")";

            md.append(indent).append(edge.getSourceClass()).append(arrow)
                    .append(edge.getTargetFieldType()).append(": ").append(callLabel).append("\n");

            if (edge.hasSubCalls()) {
                renderEdges(edge.getSubCalls(), md, rendered);
            }

            md.append(indent).append(edge.getTargetFieldType()).append("-->>")
                    .append(edge.getSourceClass()).append(": response\n");
        }

        if (currentCondition != null) {
            md.append("    end\n");
        }
    }

    private void collectParticipants(CallGraphModel graph, Set<String> participants) {
        for (CallGraphModel.CallEdge edge : graph.getEdges()) {
            participants.add(edge.getTargetFieldType());
            if (edge.hasSubCalls()) {
                collectParticipants(edge.getSubCalls(), participants);
            }
        }
    }

    public String generateERDiagram(List<EntityModel> entities) {
        if (entities == null || entities.isEmpty()) return null;

        StringBuilder md = new StringBuilder();
        md.append("```mermaid\nerDiagram\n");

        for (EntityModel entity : entities) {
            String entityName = entity.getDisplayName();
            md.append("    ").append(entityName).append(" {\n");
            for (FieldModel field : entity.getFields()) {
                md.append("        ").append(field.getTypeName()).append(" ").append(field.getName());
                if (field.isPrimaryKey()) {
                    md.append(" PK");
                }
                md.append("\n");
            }
            md.append("    }\n");
        }

        md.append("\n");

        for (EntityModel entity : entities) {
            for (RelationModel relation : entity.getRelations()) {
                md.append("    ").append(entity.getDisplayName())
                        .append(" ").append(relation.getMermaidRelation()).append(" ")
                        .append(relation.getTargetEntityName())
                        .append(" : ").append(relation.getFieldName()).append("\n");
            }
        }

        md.append("```\n");
        return md.toString();
    }

    public String generateEventFlowDiagram(List<EventModel> events) {
        if (events == null || events.isEmpty()) return null;

        StringBuilder md = new StringBuilder();
        md.append("```mermaid\ngraph LR\n");

        Set<String> declaredNodes = new LinkedHashSet<>();

        for (EventModel event : events) {
            String eventId = sanitizeId(event.getDisplayName());
            if (!declaredNodes.contains(eventId)) {
                md.append("    ").append(eventId).append("([\"").append(event.getDisplayName()).append("\"])\n");
                declaredNodes.add(eventId);
            }

            for (ProducerModel producer : event.getProducers()) {
                String producerId = sanitizeId(producer.getClassName());
                if (!declaredNodes.contains(producerId)) {
                    md.append("    ").append(producerId).append("[\"").append(producer.getClassName()).append("\"]\n");
                    declaredNodes.add(producerId);
                }
                md.append("    ").append(producerId).append(" -->|produce| ").append(eventId).append("\n");
            }

            for (ConsumerModel consumer : event.getConsumers()) {
                String consumerId = sanitizeId(consumer.getClassName());
                if (!declaredNodes.contains(consumerId)) {
                    md.append("    ").append(consumerId).append("[\"").append(consumer.getClassName()).append("\"]\n");
                    declaredNodes.add(consumerId);
                }
                md.append("    ").append(eventId).append(" -->|consume| ").append(consumerId).append("\n");
            }
        }

        md.append("```\n");
        return md.toString();
    }

    public String generateERDiagramSimplified(List<EntityModel> entities) {
        if (entities == null || entities.isEmpty()) return null;

        StringBuilder md = new StringBuilder();
        md.append("```mermaid\nerDiagram\n");

        for (EntityModel entity : entities) {
            md.append("    ").append(entity.getDisplayName()).append("\n");
        }

        md.append("\n");

        for (EntityModel entity : entities) {
            for (RelationModel relation : entity.getRelations()) {
                md.append("    ").append(entity.getDisplayName())
                        .append(" ").append(relation.getMermaidRelation()).append(" ")
                        .append(relation.getTargetEntityName())
                        .append(" : ").append(relation.getFieldName()).append("\n");
            }
        }

        md.append("```\n");
        return md.toString();
    }

    public String generateDependencyMap(String serviceName, List<ExternalServiceModel> services) {
        if (services == null || services.isEmpty()) return null;

        StringBuilder md = new StringBuilder();
        md.append("```mermaid\ngraph TD\n");

        String serviceId = sanitizeId(serviceName);
        md.append("    ").append(serviceId).append("([\"").append(serviceName).append("\"])\n");
        md.append("    style ").append(serviceId).append(" fill:#4CAF50,color:#fff\n");

        for (ExternalServiceModel service : services) {
            String extId = sanitizeId(service.getName());
            md.append("    ").append(extId).append("[\"").append(service.getName()).append("\"]\n");
            md.append("    ").append(serviceId).append(" -->|").append(service.getType())
                    .append("| ").append(extId).append("\n");
        }

        md.append("```\n");
        return md.toString();
    }

    private String sanitizeId(String name) {
        return name.replaceAll("[^a-zA-Z0-9]", "_");
    }

    private Map<String, String> buildAliasMap(List<ParticipantModel> participants) {
        Map<String, String> map = new HashMap<>();
        if (participants != null) {
            for (ParticipantModel p : participants) {
                map.put(p.getName(), p.getDisplayAlias());
            }
        }
        return map;
    }
}
