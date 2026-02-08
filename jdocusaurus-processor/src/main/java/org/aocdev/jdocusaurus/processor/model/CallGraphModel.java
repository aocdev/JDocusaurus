package org.aocdev.jdocusaurus.processor.model;

import java.util.ArrayList;
import java.util.List;

public class CallGraphModel {
    private String sourceClass;
    private String sourceMethod;
    private List<CallEdge> edges = new ArrayList<>();

    public String getSourceClass() { return sourceClass; }
    public void setSourceClass(String sourceClass) { this.sourceClass = sourceClass; }

    public String getSourceMethod() { return sourceMethod; }
    public void setSourceMethod(String sourceMethod) { this.sourceMethod = sourceMethod; }

    public List<CallEdge> getEdges() { return edges; }
    public void setEdges(List<CallEdge> edges) { this.edges = edges; }

    public void addEdge(CallEdge edge) { this.edges.add(edge); }

    public boolean isEmpty() { return edges.isEmpty(); }

    public static class CallEdge {
        private String sourceClass;
        private String targetField;
        private String targetFieldType;
        private String targetMethod;
        private String arguments;
        private String condition;
        private boolean async;
        private CallGraphModel subCalls;

        public String getSourceClass() { return sourceClass; }
        public void setSourceClass(String sourceClass) { this.sourceClass = sourceClass; }

        public String getTargetField() { return targetField; }
        public void setTargetField(String targetField) { this.targetField = targetField; }

        public String getTargetFieldType() { return targetFieldType; }
        public void setTargetFieldType(String targetFieldType) { this.targetFieldType = targetFieldType; }

        public String getTargetMethod() { return targetMethod; }
        public void setTargetMethod(String targetMethod) { this.targetMethod = targetMethod; }

        public String getArguments() { return arguments; }
        public void setArguments(String arguments) { this.arguments = arguments; }

        public String getCondition() { return condition; }
        public void setCondition(String condition) { this.condition = condition; }

        public boolean isAsync() { return async; }
        public void setAsync(boolean async) { this.async = async; }

        public CallGraphModel getSubCalls() { return subCalls; }
        public void setSubCalls(CallGraphModel subCalls) { this.subCalls = subCalls; }

        public boolean hasSubCalls() { return subCalls != null && !subCalls.isEmpty(); }
    }
}
