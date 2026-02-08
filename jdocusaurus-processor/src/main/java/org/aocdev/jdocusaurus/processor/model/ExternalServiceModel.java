package org.aocdev.jdocusaurus.processor.model;

public class ExternalServiceModel {
    private String name;
    private String description;
    private String url;
    private String type;
    private String owner;
    private String usedByClass;
    private String usedByField;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public String getUsedByClass() { return usedByClass; }
    public void setUsedByClass(String usedByClass) { this.usedByClass = usedByClass; }

    public String getUsedByField() { return usedByField; }
    public void setUsedByField(String usedByField) { this.usedByField = usedByField; }
}
