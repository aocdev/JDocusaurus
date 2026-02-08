package org.aocdev.jdocusaurus.processor.generator;

import org.aocdev.jdocusaurus.processor.model.*;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class FlowMarkdownGenerator implements Generator {

    private final MermaidGenerator mermaidGenerator = new MermaidGenerator();
    private final String outputDir;

    public FlowMarkdownGenerator() { this("docs"); }

    public FlowMarkdownGenerator(String outputDir) { this.outputDir = outputDir; }

    @Override
    public void generate(ProjectModel model, Filer filer) throws IOException {
        for (FlowModel flow : model.getAllFlows()) {
            if (flow.getSteps().isEmpty()) continue;

            String fileName = toKebabCase(flow.getName()) + ".md";
            FileObject file = filer.createResource(StandardLocation.CLASS_OUTPUT, "", outputDir + "/flows/" + fileName);
            try (Writer writer = file.openWriter()) {
                writer.write(generateFlowMarkdown(flow, model.getAllParticipants()));
            }
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
