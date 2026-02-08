package org.aocdev.jdocusaurus.processor.generator;

import org.aocdev.jdocusaurus.processor.model.ExternalServiceModel;
import org.aocdev.jdocusaurus.processor.model.ProjectModel;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class DependencyMapGenerator implements Generator {

    private final MermaidGenerator mermaidGenerator = new MermaidGenerator();
    private final String serviceName;

    public DependencyMapGenerator(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public void generate(ProjectModel model, Filer filer) throws IOException {
        List<ExternalServiceModel> services = model.getExternalServices();
        if (services.isEmpty()) return;

        FileObject file = filer.createResource(StandardLocation.CLASS_OUTPUT, "", "docs/integrations/index.md");
        try (Writer writer = file.openWriter()) {
            writer.write(generateIntegrationsIndex(services));
        }
    }

    private String generateIntegrationsIndex(List<ExternalServiceModel> services) {
        StringBuilder md = new StringBuilder();

        md.append("---\n");
        md.append("sidebar_label: \"Integraciones\"\n");
        md.append("---\n\n");

        md.append("# Integraciones Externas\n\n");

        // Dependency map diagram
        String diagram = mermaidGenerator.generateDependencyMap(serviceName, services);
        if (diagram != null) {
            md.append("## Mapa de dependencias\n\n");
            md.append(diagram).append("\n");
        }

        // Table
        md.append("## Servicios externos\n\n");
        md.append("| Servicio | Tipo | URL | Responsable | Usado por |\n");
        md.append("|----------|------|-----|------------|----------|\n");
        for (ExternalServiceModel service : services) {
            md.append("| **").append(service.getName()).append("** | ");
            md.append(service.getType()).append(" | ");
            md.append(service.getUrl() != null && !service.getUrl().isEmpty()
                    ? "`" + service.getUrl() + "`" : "-").append(" | ");
            md.append(service.getOwner() != null && !service.getOwner().isEmpty()
                    ? service.getOwner() : "-").append(" | ");
            String usedBy = service.getUsedByClass();
            if (service.getUsedByField() != null && !service.getUsedByField().isEmpty()) {
                usedBy += "." + service.getUsedByField();
            }
            md.append("`").append(usedBy).append("` |\n");
        }
        md.append("\n");

        // Descriptions
        for (ExternalServiceModel service : services) {
            if (service.getDescription() != null && !service.getDescription().isEmpty()) {
                md.append("### ").append(service.getName()).append("\n\n");
                md.append(service.getDescription()).append("\n\n");
            }
        }

        return md.toString();
    }
}
