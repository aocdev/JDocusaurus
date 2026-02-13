package org.aocdev.jdocusaurus.processor.generator;

import org.aocdev.jdocusaurus.processor.model.*;

import java.io.IOException;
import java.util.List;

/**
 * Generates data model documentation with ER diagrams and entity detail pages.
 *
 * <p>Output: {@code data-model/index.md} (global ER diagram + summary table)
 * and {@code data-model/{kebab-case-name}.md} per entity.
 *
 * @since 1.0.0
 */
public class EntityMarkdownGenerator implements Generator {

    private final MermaidGenerator mermaidGenerator = new MermaidGenerator();

    @Override
    public void generate(ProjectModel model, DocWriter writer) throws IOException {
        List<EntityModel> entities = model.getEntities();
        if (entities.isEmpty()) return;

        writer.write("data-model/index.md", generateIndex(entities));

        for (EntityModel entity : entities) {
            String fileName = toKebabCase(entity.getClassName()) + ".md";
            writer.write("data-model/" + fileName, generateEntityMarkdown(entity));
        }
    }

    private String generateIndex(List<EntityModel> entities) {
        StringBuilder md = new StringBuilder();

        md.append("---\n");
        md.append("sidebar_label: \"Modelo de Datos\"\n");
        md.append("---\n\n");

        md.append("# Modelo de Datos\n\n");

        String erDiagram = mermaidGenerator.generateERDiagram(entities);
        if (erDiagram != null) {
            md.append("## Diagrama ER\n\n");
            md.append(erDiagram).append("\n");
        }

        md.append("## Entidades\n\n");
        md.append("| Entidad | Tabla | Descripcion | Campos | Relaciones |\n");
        md.append("|---------|-------|------------|--------|------------|\n");
        for (EntityModel entity : entities) {
            md.append("| [").append(entity.getDisplayName()).append("](./")
                    .append(toKebabCase(entity.getClassName())).append(") | ");
            md.append(entity.getTableName() != null && !entity.getTableName().isEmpty()
                    ? "`" + entity.getTableName() + "`" : "-").append(" | ");
            md.append(entity.getDescription() != null ? entity.getDescription() : "-").append(" | ");
            md.append(entity.getFields().size()).append(" | ");
            md.append(entity.getRelations().size()).append(" |\n");
        }
        md.append("\n");

        return md.toString();
    }

    private String generateEntityMarkdown(EntityModel entity) {
        StringBuilder md = new StringBuilder();

        md.append("---\n");
        md.append("sidebar_label: \"").append(entity.getDisplayName()).append("\"\n");
        md.append("---\n\n");

        md.append("# ").append(entity.getDisplayName()).append("\n\n");

        if (entity.getDescription() != null && !entity.getDescription().isEmpty()) {
            md.append(entity.getDescription()).append("\n\n");
        }

        if (entity.getTableName() != null && !entity.getTableName().isEmpty()) {
            md.append("**Tabla:** `").append(entity.getTableName()).append("`\n\n");
        }

        if (!entity.getFields().isEmpty()) {
            md.append("## Campos\n\n");
            md.append("| Nombre | Tipo | Nullable | Descripcion | Constraints |\n");
            md.append("|--------|------|----------|------------|-------------|\n");
            for (FieldModel field : entity.getFields()) {
                md.append("| ");
                if (field.isPrimaryKey()) {
                    md.append("**`").append(field.getName()).append("`** (PK)");
                } else {
                    md.append("`").append(field.getName()).append("`");
                }
                md.append(" | `").append(field.getTypeName()).append("` | ");
                md.append(field.isNullable() ? "Si" : "No").append(" | ");
                md.append(field.getDescription() != null && !field.getDescription().isEmpty()
                        ? field.getDescription() : "-");
                if (field.getExample() != null && !field.getExample().isEmpty()) {
                    md.append(" (ej: `").append(field.getExample()).append("`)");
                }
                md.append(" | ");
                md.append(field.getConstraints() != null && !field.getConstraints().isEmpty()
                        ? field.getConstraints() : "-");
                md.append(" |\n");
            }
            md.append("\n");
        }

        if (!entity.getRelations().isEmpty()) {
            md.append("## Relaciones\n\n");
            md.append("| Campo | Tipo | Entidad destino | Descripcion |\n");
            md.append("|-------|------|----------------|-------------|\n");
            for (RelationModel relation : entity.getRelations()) {
                md.append("| `").append(relation.getFieldName()).append("` | ");
                md.append(relation.getType()).append(" | ");
                md.append(relation.getTargetEntityName()).append(" | ");
                md.append(relation.getDescription() != null && !relation.getDescription().isEmpty()
                        ? relation.getDescription() : "-");
                md.append(" |\n");
            }
            md.append("\n");
        }

        return md.toString();
    }

    private String toKebabCase(String name) {
        return name.replaceAll("([a-z])([A-Z])", "$1-$2")
                .replaceAll("[\\s_]+", "-")
                .toLowerCase();
    }
}
