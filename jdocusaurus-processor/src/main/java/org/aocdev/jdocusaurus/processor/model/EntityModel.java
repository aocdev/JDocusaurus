package org.aocdev.jdocusaurus.processor.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model representing a {@code @JDocEntity}-annotated data entity.
 *
 * <p>Contains entity metadata, fields, and relationships. Used to generate
 * entity pages and the Mermaid ER diagram. May be enriched with JPA metadata
 * by {@link org.aocdev.jdocusaurus.processor.scanner.JpaScanner}.
 *
 * @since 1.0.0
 */
public class EntityModel {
    private String className;
    private String name;
    private String description;
    private String tableName;
    private List<FieldModel> fields = new ArrayList<>();
    private List<RelationModel> relations = new ArrayList<>();

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }

    public List<FieldModel> getFields() { return fields; }
    public void setFields(List<FieldModel> fields) { this.fields = fields; }

    public List<RelationModel> getRelations() { return relations; }
    public void setRelations(List<RelationModel> relations) { this.relations = relations; }

    public String getDisplayName() {
        return (name != null && !name.isEmpty()) ? name : className;
    }
}
