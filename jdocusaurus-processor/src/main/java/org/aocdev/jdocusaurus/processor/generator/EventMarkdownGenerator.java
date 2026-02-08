package org.aocdev.jdocusaurus.processor.generator;

import org.aocdev.jdocusaurus.processor.model.*;

import java.io.IOException;
import java.util.List;

public class EventMarkdownGenerator implements Generator {

    private final MermaidGenerator mermaidGenerator = new MermaidGenerator();

    @Override
    public void generate(ProjectModel model, DocWriter writer) throws IOException {
        List<EventModel> events = model.getEvents();
        if (events.isEmpty()) return;

        writer.write("events/index.md", generateIndex(events));

        for (EventModel event : events) {
            String fileName = toKebabCase(event.getDisplayName()) + ".md";
            writer.write("events/" + fileName, generateEventMarkdown(event));
        }
    }

    private String generateIndex(List<EventModel> events) {
        StringBuilder md = new StringBuilder();

        md.append("---\n");
        md.append("sidebar_label: \"Eventos\"\n");
        md.append("---\n\n");

        md.append("# Eventos\n\n");

        String diagram = mermaidGenerator.generateEventFlowDiagram(events);
        if (diagram != null) {
            md.append("## Mapa de eventos\n\n");
            md.append(diagram).append("\n");
        }

        md.append("## Resumen\n\n");
        md.append("| Evento | Topic | Productores | Consumidores | Descripcion |\n");
        md.append("|--------|-------|-------------|-------------|-------------|\n");
        for (EventModel event : events) {
            md.append("| [").append(event.getDisplayName()).append("](./")
                    .append(toKebabCase(event.getDisplayName())).append(") | ");
            md.append(event.getTopic() != null && !event.getTopic().isEmpty()
                    ? "`" + event.getTopic() + "`" : "-").append(" | ");
            md.append(event.getProducers().size()).append(" | ");
            md.append(event.getConsumers().size()).append(" | ");
            md.append(event.getDescription() != null && !event.getDescription().isEmpty()
                    ? event.getDescription() : "-");
            md.append(" |\n");
        }
        md.append("\n");

        return md.toString();
    }

    private String generateEventMarkdown(EventModel event) {
        StringBuilder md = new StringBuilder();

        md.append("---\n");
        md.append("sidebar_label: \"").append(event.getDisplayName()).append("\"\n");
        md.append("---\n\n");

        md.append("# ").append(event.getDisplayName()).append("\n\n");

        if (event.getDescription() != null && !event.getDescription().isEmpty()) {
            md.append(event.getDescription()).append("\n\n");
        }

        if (event.getTopic() != null && !event.getTopic().isEmpty()) {
            md.append("**Topic:** `").append(event.getTopic()).append("`\n\n");
        }

        if (event.getSchema() != null && !event.getSchema().isEmpty()) {
            md.append("**Schema:**\n\n");
            md.append("```json\n").append(event.getSchema()).append("\n```\n\n");
        }

        if (!event.getProducers().isEmpty()) {
            md.append("## Productores\n\n");
            md.append("| Clase | Metodo | Descripcion | Async |\n");
            md.append("|-------|--------|------------|-------|\n");
            for (ProducerModel producer : event.getProducers()) {
                md.append("| `").append(producer.getClassName()).append("` | ");
                md.append(producer.getMethodName() != null && !producer.getMethodName().isEmpty()
                        ? "`" + producer.getMethodName() + "()`" : "-").append(" | ");
                md.append(producer.getDescription() != null && !producer.getDescription().isEmpty()
                        ? producer.getDescription() : "-").append(" | ");
                md.append(producer.isAsync() ? "Si" : "No").append(" |\n");
            }
            md.append("\n");
        }

        if (!event.getConsumers().isEmpty()) {
            md.append("## Consumidores\n\n");
            md.append("| Clase | Metodo | Descripcion | Consumer Group |\n");
            md.append("|-------|--------|------------|----------------|\n");
            for (ConsumerModel consumer : event.getConsumers()) {
                md.append("| `").append(consumer.getClassName()).append("` | ");
                md.append(consumer.getMethodName() != null && !consumer.getMethodName().isEmpty()
                        ? "`" + consumer.getMethodName() + "()`" : "-").append(" | ");
                md.append(consumer.getDescription() != null && !consumer.getDescription().isEmpty()
                        ? consumer.getDescription() : "-").append(" | ");
                md.append(consumer.getGroup() != null && !consumer.getGroup().isEmpty()
                        ? "`" + consumer.getGroup() + "`" : "-").append(" |\n");
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
