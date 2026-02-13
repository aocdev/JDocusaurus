package org.aocdev.jdocusaurus.processor.generator;

import org.aocdev.jdocusaurus.processor.model.ConfigModel;
import org.aocdev.jdocusaurus.processor.model.ProjectModel;

import java.io.IOException;
import java.util.List;

/**
 * Generates configuration properties documentation.
 *
 * <p>Output: {@code config/index.md} with a properties table. Values
 * marked as {@code secret = true} are masked with {@code ***}.
 *
 * @since 1.0.0
 */
public class ConfigMarkdownGenerator implements Generator {

    @Override
    public void generate(ProjectModel model, DocWriter writer) throws IOException {
        List<ConfigModel> configs = model.getConfigs();
        if (configs.isEmpty()) return;

        writer.write("config/index.md", generateConfigIndex(configs));
    }

    private String generateConfigIndex(List<ConfigModel> configs) {
        StringBuilder md = new StringBuilder();

        md.append("---\n");
        md.append("sidebar_label: \"Configuracion\"\n");
        md.append("---\n\n");

        md.append("# Configuracion\n\n");

        md.append("| Propiedad | Descripcion | Default | Obligatoria | Secreto | Ejemplo |\n");
        md.append("|-----------|------------|---------|-------------|---------|--------|\n");
        for (ConfigModel config : configs) {
            md.append("| `").append(config.getKey()).append("` | ");
            md.append(config.getDescription() != null && !config.getDescription().isEmpty()
                    ? config.getDescription() : "-").append(" | ");
            md.append(config.getDefaultValue() != null && !config.getDefaultValue().isEmpty()
                    ? "`" + config.getDefaultValue() + "`" : "-").append(" | ");
            md.append(config.isRequired() ? "Si" : "No").append(" | ");
            md.append(config.isSecret() ? "Si" : "No").append(" | ");
            if (config.isSecret()) {
                md.append("***");
            } else {
                md.append(config.getExample() != null && !config.getExample().isEmpty()
                        ? "`" + config.getExample() + "`" : "-");
            }
            md.append(" |\n");
        }
        md.append("\n");

        return md.toString();
    }
}
