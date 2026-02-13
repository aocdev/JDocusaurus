package org.aocdev.jdocusaurus.processor.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model representing a {@code @JDocEvent}-annotated domain event.
 *
 * <p>Contains the event metadata and its associated producers and consumers,
 * used to generate event pages and the Mermaid event flow graph.
 *
 * @since 1.0.0
 */
public class EventModel {
    private String className;
    private String name;
    private String description;
    private String topic;
    private String schema;
    private List<ProducerModel> producers = new ArrayList<>();
    private List<ConsumerModel> consumers = new ArrayList<>();

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getSchema() { return schema; }
    public void setSchema(String schema) { this.schema = schema; }

    public List<ProducerModel> getProducers() { return producers; }
    public void setProducers(List<ProducerModel> producers) { this.producers = producers; }
    public void addProducer(ProducerModel producer) { this.producers.add(producer); }

    public List<ConsumerModel> getConsumers() { return consumers; }
    public void setConsumers(List<ConsumerModel> consumers) { this.consumers = consumers; }
    public void addConsumer(ConsumerModel consumer) { this.consumers.add(consumer); }

    public String getDisplayName() {
        return (name != null && !name.isEmpty()) ? name : className;
    }
}
