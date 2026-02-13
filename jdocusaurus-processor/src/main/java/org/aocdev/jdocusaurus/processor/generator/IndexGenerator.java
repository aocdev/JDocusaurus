package org.aocdev.jdocusaurus.processor.generator;

import org.aocdev.jdocusaurus.processor.config.JDocusaurusConfig;
import org.aocdev.jdocusaurus.processor.model.*;

import java.io.IOException;
import java.util.List;

/**
 * Generates index pages for the Docusaurus documentation structure.
 *
 * <p>Output: {@code index.md} (global overview with summary diagrams),
 * {@code api/index.md} (controller listing), and {@code flows/index.md}
 * (flow listing).
 *
 * @since 1.0.0
 */
public class IndexGenerator implements Generator {

    private final JDocusaurusConfig config;
    private final MermaidGenerator mermaidGenerator = new MermaidGenerator();

    public IndexGenerator(JDocusaurusConfig config) {
        this.config = config;
    }

    @Override
    public void generate(ProjectModel model, DocWriter writer) throws IOException {
        // Global index
        writer.write("index.md", generateGlobalIndex(model));

        // API index
        if (!model.getClasses().isEmpty()) {
            writer.write("api/index.md", generateApiIndex(model.getClasses()));
        }

        // Flows index
        if (!model.getAllFlows().isEmpty()) {
            writer.write("flows/index.md", generateFlowsIndex(model.getAllFlows()));
        }
    }

    private String generateGlobalIndex(ProjectModel model) {
        StringBuilder md = new StringBuilder();

        String name = config.getProjectName().isEmpty() ? "Documentacion" : config.getProjectName();
        String description = config.getProjectDescription();

        md.append("---\n");
        md.append("sidebar_label: \"Overview\"\n");
        md.append("sidebar_position: 0\n");
        md.append("---\n\n");

        md.append("# ").append(name).append("\n\n");

        if (!description.isEmpty()) {
            md.append(description).append("\n\n");
        }

        // Architecture diagram
        if (!model.getExternalServices().isEmpty()) {
            String serviceName = config.getProjectName().isEmpty()
                    ? (model.getClasses().isEmpty() ? "Service" : model.getClasses().get(0).getName())
                    : config.getProjectName();
            String diagram = mermaidGenerator.generateDependencyMap(serviceName, model.getExternalServices());
            if (diagram != null) {
                md.append("## Arquitectura\n\n");
                md.append(diagram).append("\n");
            }
        }

        // Simplified ER diagram
        if (!model.getEntities().isEmpty()) {
            String erDiagram = mermaidGenerator.generateERDiagramSimplified(model.getEntities());
            if (erDiagram != null) {
                md.append("## Modelo de Datos\n\n");
                md.append(erDiagram).append("\n");
            }
        }

        // Event flow diagram
        if (!model.getEvents().isEmpty()) {
            String eventDiagram = mermaidGenerator.generateEventFlowDiagram(model.getEvents());
            if (eventDiagram != null) {
                md.append("## Eventos\n\n");
                md.append(eventDiagram).append("\n");
            }
        }

        // Summary
        md.append("## Resumen\n\n");
        md.append("| Seccion | Cantidad |\n");
        md.append("|---------|----------|\n");

        int endpointCount = model.getClasses().stream()
                .mapToInt(c -> c.getEndpoints().size()).sum();

        if (!model.getClasses().isEmpty()) {
            md.append("| [API](./api/) | ").append(model.getClasses().size())
                    .append(" controladores, ").append(endpointCount).append(" endpoints |\n");
        }
        if (!model.getAllFlows().isEmpty()) {
            md.append("| [Flujos](./flows/) | ").append(model.getAllFlows().size()).append(" flujos |\n");
        }
        if (!model.getEntities().isEmpty()) {
            md.append("| [Modelo de Datos](./data-model/) | ").append(model.getEntities().size()).append(" entidades |\n");
        }
        if (!model.getEvents().isEmpty()) {
            md.append("| [Eventos](./events/) | ").append(model.getEvents().size()).append(" eventos |\n");
        }
        if (!model.getBusinessRules().isEmpty()) {
            md.append("| [Reglas de Negocio](./business-rules/) | ").append(model.getBusinessRules().size()).append(" reglas |\n");
        }
        if (!model.getExternalServices().isEmpty()) {
            md.append("| [Integraciones](./integrations/) | ").append(model.getExternalServices().size()).append(" servicios |\n");
        }
        if (!model.getConfigs().isEmpty()) {
            md.append("| [Configuracion](./config/) | ").append(model.getConfigs().size()).append(" propiedades |\n");
        }
        md.append("\n");

        return md.toString();
    }

    private String generateApiIndex(List<ClassModel> classes) {
        StringBuilder md = new StringBuilder();

        md.append("---\n");
        md.append("sidebar_label: \"API\"\n");
        md.append("sidebar_position: 1\n");
        md.append("---\n\n");

        md.append("# API\n\n");

        md.append("| Controlador | Base Path | Endpoints | Descripcion |\n");
        md.append("|-------------|-----------|-----------|-------------|\n");
        for (ClassModel classModel : classes) {
            md.append("| [").append(classModel.getDisplayName()).append("](./")
                    .append(toKebabCase(classModel.getClassName())).append(") | ");
            md.append(classModel.getBasePath().isEmpty() ? "-" : "`" + classModel.getBasePath() + "`").append(" | ");
            md.append(classModel.getEndpoints().size()).append(" | ");
            md.append(classModel.getDescription()).append(" |\n");
        }
        md.append("\n");

        // Endpoint summary table
        md.append("## Todos los endpoints\n\n");
        md.append("| Metodo | Path | Descripcion | Controlador |\n");
        md.append("|--------|------|-------------|-------------|\n");
        for (ClassModel classModel : classes) {
            for (EndpointModel endpoint : classModel.getEndpoints()) {
                String fullPath = classModel.getBasePath() + endpoint.getPath();
                md.append("| `").append(endpoint.getHttpMethod()).append("` | `").append(fullPath).append("` | ");
                md.append(endpoint.getSummary() != null && !endpoint.getSummary().isEmpty()
                        ? endpoint.getSummary() : endpoint.getDescription()).append(" | ");
                md.append(classModel.getDisplayName()).append(" |\n");
            }
        }
        md.append("\n");

        return md.toString();
    }

    private String generateFlowsIndex(List<FlowModel> flows) {
        StringBuilder md = new StringBuilder();

        md.append("---\n");
        md.append("sidebar_label: \"Flujos\"\n");
        md.append("sidebar_position: 2\n");
        md.append("---\n\n");

        md.append("# Flujos\n\n");

        md.append("| Flujo | Descripcion | Pasos |\n");
        md.append("|-------|------------|-------|\n");
        for (FlowModel flow : flows) {
            md.append("| [").append(flow.getDisplayTitle()).append("](./")
                    .append(toKebabCase(flow.getName())).append(") | ");
            md.append(flow.getDescription() != null && !flow.getDescription().isEmpty()
                    ? flow.getDescription() : "-").append(" | ");
            md.append(flow.getSteps().size()).append(" |\n");
        }
        md.append("\n");

        return md.toString();
    }

    private String toKebabCase(String name) {
        return name.replaceAll("([a-z])([A-Z])", "$1-$2")
                .replaceAll("[\\s_]+", "-")
                .toLowerCase();
    }
}
