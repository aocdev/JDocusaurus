package org.aocdev.jdocusaurus.processor.model;

/**
 * Model representing a participant (column) in a Mermaid sequence diagram.
 *
 * @since 1.0.0
 */
public class ParticipantModel {
    private String name;
    private String alias;
    private String type;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDisplayAlias() {
        return (alias != null && !alias.isEmpty()) ? alias : name;
    }
}
