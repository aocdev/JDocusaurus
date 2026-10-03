/*
 * Copyright 2026 aocdev (Albert Ortells)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
