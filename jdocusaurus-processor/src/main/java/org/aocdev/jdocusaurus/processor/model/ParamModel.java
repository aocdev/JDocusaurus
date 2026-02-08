package org.aocdev.jdocusaurus.processor.model;

public class ParamModel {
    private String name;
    private String description;
    private String location;
    private boolean required;
    private String defaultValue;
    private String example;
    private String type;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }

    public String getDefaultValue() { return defaultValue; }
    public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }

    public String getExample() { return example; }
    public void setExample(String example) { this.example = example; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
