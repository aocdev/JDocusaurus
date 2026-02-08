package org.aocdev.jdocusaurus.processor.model;

public class RelationModel {
    private String fieldName;
    private String targetEntityName;
    private String type;
    private String description;

    public String getFieldName() { return fieldName; }
    public void setFieldName(String fieldName) { this.fieldName = fieldName; }

    public String getTargetEntityName() { return targetEntityName; }
    public void setTargetEntityName(String targetEntityName) { this.targetEntityName = targetEntityName; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getMermaidRelation() {
        return switch (type) {
            case "ONE_TO_ONE" -> "||--||";
            case "ONE_TO_MANY" -> "||--o{";
            case "MANY_TO_ONE" -> "}o--||";
            case "MANY_TO_MANY" -> "}o--o{";
            default -> "||--||";
        };
    }
}
