package org.aocdev.jdocusaurus.processor.model;

import java.util.ArrayList;
import java.util.List;

public class ClassModel {
    private String className;
    private String name;
    private String description;
    private String basePath;
    private String version;
    private String group;
    private String[] tags;
    private List<EndpointModel> endpoints = new ArrayList<>();
    private ParticipantModel participant;
    private List<FlowModel> flows = new ArrayList<>();
    private String qualifiedName;

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBasePath() { return basePath; }
    public void setBasePath(String basePath) { this.basePath = basePath; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; }

    public String[] getTags() { return tags; }
    public void setTags(String[] tags) { this.tags = tags; }

    public List<EndpointModel> getEndpoints() { return endpoints; }
    public void setEndpoints(List<EndpointModel> endpoints) { this.endpoints = endpoints; }

    public String getDisplayName() {
        return (name != null && !name.isEmpty()) ? name : className;
    }

    public ParticipantModel getParticipant() { return participant; }
    public void setParticipant(ParticipantModel participant) { this.participant = participant; }

    public List<FlowModel> getFlows() { return flows; }
    public void setFlows(List<FlowModel> flows) { this.flows = flows; }

    public String getQualifiedName() { return qualifiedName; }
    public void setQualifiedName(String qualifiedName) { this.qualifiedName = qualifiedName; }
}
