package org.aocdev.jdocusaurus.processor.model;

/**
 * Model representing an HTTP response for an {@link EndpointModel}.
 *
 * @since 1.0.0
 */
public class ResponseModel {
    private int code;
    private String description;
    private String typeName;
    private String example;

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }

    public String getExample() { return example; }
    public void setExample(String example) { this.example = example; }
}
