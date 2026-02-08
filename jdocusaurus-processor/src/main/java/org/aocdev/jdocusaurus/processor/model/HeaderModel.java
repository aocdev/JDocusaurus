package org.aocdev.jdocusaurus.processor.model;

public class HeaderModel {
    private String name;
    private String description;
    private boolean required;
    private String direction;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }

    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }
}
