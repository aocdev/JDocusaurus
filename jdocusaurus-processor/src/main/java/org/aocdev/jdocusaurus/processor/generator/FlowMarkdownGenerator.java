package org.aocdev.jdocusaurus.processor.generator;

import org.aocdev.jdocusaurus.processor.model.*;

import java.io.IOException;
import java.util.List;

/**
 * Generates per-flow Markdown pages with Mermaid sequence diagrams.
 *
 * <p>Output: {@code flows/{kebab-case-name}.md} for each declared flow.
 *
 * @since 1.0.0
 */
public class FlowMarkdownGenerator implements Generator {

    private final MermaidGenerator mermaidGenerator = new MermaidGenerator();

    @Override
    public void generate(ProjectModel model, DocWriter writer) throws IOException {
        for (FlowModel flow : model.getAllFlows()) {
            if (flow.getSteps().isEmpty()) continue;

            String fileName = toKebabCase(flow.getName()) + ".md";
            writer.write("flows/" + fileName, generateFlowMarkdown(flow, model.getAllParticipants()));
        }
    }

    private String generateFlowMarkdown(FlowModel flow, List<ParticipantModel> participants) {
        StringBuilder md = new StringBuilder();

        md.append("---\n");
        md.append("sidebar_label: \"").append(flow.getDisplayTitle()).append("\"\n");
        md.append("---\n\n");

        md.append("# ").append(flow.getDisplayTitle()).append("\n\n");

        if (flow.getDescription() != null && !flow.getDescription().isEmpty()) {
            md.append(flow.getDescription()).append("\n\n");
        }

        String diagram = mermaidGenerator.generateSequenceDiagramFromManualSteps(flow.getSteps(), participants);
        if (diagram != null) {
            md.append("## Diagrama de secuencia\n\n");
            md.append(diagram).append("\n");
        }

        return md.toString();
    }

    private String toKebabCase(String name) {
        return name.replaceAll("([a-z])([A-Z])", "$1-$2")
                .replaceAll("[\\s_]+", "-")
                .toLowerCase();
    }
}
