package org.aocdev.jdocusaurus.processor.generator;

import org.aocdev.jdocusaurus.processor.model.*;

import java.io.IOException;
import java.util.List;

/**
 * Generates per-controller Markdown pages with endpoint documentation.
 *
 * <p>Output: {@code api/{kebab-case-name}.md} for each {@code @JDocClass}.
 * Includes endpoint tables, parameter/response/header details, deprecation
 * badges, and embedded Mermaid sequence diagrams.
 *
 * @since 1.0.0
 */
public class MarkdownGenerator implements Generator {

    private final MermaidGenerator mermaidGenerator = new MermaidGenerator();

    @Override
    public void generate(ProjectModel model, DocWriter writer) throws IOException {
        for (ClassModel classModel : model.getClasses()) {
            String fileName = toKebabCase(classModel.getClassName()) + ".md";
            writer.write("api/" + fileName, generateClassMarkdown(classModel, model.getAllParticipants()));
        }
    }

    private String generateClassMarkdown(ClassModel classModel, List<ParticipantModel> participants) {
        StringBuilder md = new StringBuilder();

        // Frontmatter
        md.append("---\n");
        md.append("sidebar_label: \"").append(classModel.getDisplayName()).append("\"\n");
        md.append("---\n\n");

        // Title
        md.append("# ").append(classModel.getDisplayName()).append("\n\n");

        // Description
        md.append(classModel.getDescription()).append("\n\n");

        // Base path
        if (!classModel.getBasePath().isEmpty()) {
            md.append("**Base Path:** `").append(classModel.getBasePath()).append("`\n\n");
        }

        // Version
        if (!classModel.getVersion().isEmpty()) {
            md.append("**Version:** ").append(classModel.getVersion()).append("\n\n");
        }

        // Endpoints
        for (EndpointModel endpoint : classModel.getEndpoints()) {
            md.append(generateEndpointMarkdown(endpoint, participants));
        }

        return md.toString();
    }

    private String generateEndpointMarkdown(EndpointModel endpoint, List<ParticipantModel> participants) {
        StringBuilder md = new StringBuilder();

        // Title
        md.append("---\n\n");
        md.append("## ").append(endpoint.getHttpMethod()).append(" ").append(endpoint.getPath()).append("\n\n");

        // Deprecated badge
        if (endpoint.isDeprecated()) {
            md.append("> **DEPRECATED**");
            if (!endpoint.getDeprecatedMessage().isEmpty()) {
                md.append(": ").append(endpoint.getDeprecatedMessage());
            }
            md.append("\n\n");
        }

        // Description
        md.append(endpoint.getDescription()).append("\n\n");

        // Auth
        if (!endpoint.getAuth().isEmpty()) {
            md.append("**Autenticacion:** ").append(endpoint.getAuth()).append("\n\n");
        }

        // Headers
        if (!endpoint.getHeaders().isEmpty()) {
            md.append("### Headers\n\n");
            md.append("| Nombre | Descripcion | Obligatorio | Direccion |\n");
            md.append("|--------|------------|-------------|----------|\n");
            for (HeaderModel header : endpoint.getHeaders()) {
                md.append("| `").append(header.getName()).append("` | ");
                md.append(header.getDescription()).append(" | ");
                md.append(header.isRequired() ? "Si" : "No").append(" | ");
                md.append(header.getDirection()).append(" |\n");
            }
            md.append("\n");
        }

        // Parameters
        md.append("### Parametros\n\n");
        if (endpoint.getParams().isEmpty()) {
            md.append("Ninguno.\n\n");
        } else {
            md.append("| Nombre | Ubicacion | Tipo | Obligatorio | Descripcion |\n");
            md.append("|--------|-----------|------|-------------|-------------|\n");
            for (ParamModel param : endpoint.getParams()) {
                md.append("| `").append(param.getName()).append("` | ");
                md.append(param.getLocation()).append(" | ");
                md.append("`").append(simplifyTypeName(param.getType())).append("` | ");
                md.append(param.isRequired() ? "Si" : "No").append(" | ");
                md.append(param.getDescription());
                if (!param.getExample().isEmpty()) {
                    md.append(" (ej: `").append(param.getExample()).append("`)");
                }
                md.append(" |\n");
            }
            md.append("\n");
        }

        // Responses
        if (!endpoint.getResponses().isEmpty()) {
            md.append("### Respuestas\n\n");
            md.append("| Codigo | Descripcion | Tipo |\n");
            md.append("|--------|------------|------|\n");
            for (ResponseModel response : endpoint.getResponses()) {
                md.append("| `").append(response.getCode()).append("` | ");
                md.append(response.getDescription()).append(" | ");
                String typeName = response.getTypeName();
                if (typeName != null && !typeName.equals("Void") && !typeName.equals("void")) {
                    md.append("`").append(typeName).append("`");
                } else {
                    md.append("-");
                }
                md.append(" |\n");
            }
            md.append("\n");
        }

        // Sequence diagram (auto or manual)
        if (endpoint.hasFlowDiagram()) {
            md.append("### Diagrama de secuencia\n\n");
            if (endpoint.getCallGraph() != null && !endpoint.getCallGraph().isEmpty()) {
                String diagram = mermaidGenerator.generateSequenceDiagramFromCallGraph(
                        endpoint.getCallGraph(), endpoint.getHttpMethod(), endpoint.getPath(), participants);
                if (diagram != null) {
                    md.append(diagram).append("\n");
                }
            } else if (!endpoint.getManualSteps().isEmpty()) {
                String diagram = mermaidGenerator.generateSequenceDiagramFromManualSteps(
                        endpoint.getManualSteps(), participants);
                if (diagram != null) {
                    md.append(diagram).append("\n");
                }
            }
        }

        return md.toString();
    }

    private String toKebabCase(String name) {
        return name.replaceAll("([a-z])([A-Z])", "$1-$2").toLowerCase();
    }

    private String simplifyTypeName(String fullType) {
        if (fullType == null || fullType.isEmpty()) return "Object";
        int lastDot = fullType.lastIndexOf('.');
        return lastDot >= 0 ? fullType.substring(lastDot + 1) : fullType;
    }
}
