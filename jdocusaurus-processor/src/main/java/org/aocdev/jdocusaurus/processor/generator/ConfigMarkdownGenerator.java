package org.aocdev.jdocusaurus.processor.generator;

import org.aocdev.jdocusaurus.processor.model.ConfigModel;
import org.aocdev.jdocusaurus.processor.model.ProjectModel;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class ConfigMarkdownGenerator implements Generator {

    private final String outputDir;

    public ConfigMarkdownGenerator() { this("docs"); }

    public ConfigMarkdownGenerator(String outputDir) { this.outputDir = outputDir; }

    @Override
    public void generate(ProjectModel model, Filer filer) throws IOException {
        List<ConfigModel> configs = model.getConfigs();
        if (configs.isEmpty()) return;

        FileObject file = filer.createResource(StandardLocation.CLASS_OUTPUT, "", outputDir + "/config/index.md");
        try (Writer writer = file.openWriter()) {
            writer.write(generateConfigIndex(configs));
        }
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
