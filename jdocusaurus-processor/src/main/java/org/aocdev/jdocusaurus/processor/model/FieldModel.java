package org.aocdev.jdocusaurus.processor.model;

/**
 * Model representing a field within an {@link EntityModel}.
 *
 * <p>May be populated from {@code @JDocField} annotations or enriched
 * with JPA {@code @Column} / {@code @Id} metadata.
 *
 * @since 1.0.0
 */
public class FieldModel {
    private String name;
    private String typeName;
    private String description;
    private String example;
    private boolean nullable = true;
    private String constraints;
    private boolean primaryKey;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getExample() { return example; }
    public void setExample(String example) { this.example = example; }

    public boolean isNullable() { return nullable; }
    public void setNullable(boolean nullable) { this.nullable = nullable; }

    public String getConstraints() { return constraints; }
    public void setConstraints(String constraints) { this.constraints = constraints; }

    public boolean isPrimaryKey() { return primaryKey; }
    public void setPrimaryKey(boolean primaryKey) { this.primaryKey = primaryKey; }
}
