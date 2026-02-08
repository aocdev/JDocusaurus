package org.aocdev.jdocusaurus.processor.model;

import java.util.ArrayList;
import java.util.List;

public class ProjectModel {
    private List<ClassModel> classes = new ArrayList<>();
    private List<FlowModel> allFlows = new ArrayList<>();
    private List<ParticipantModel> allParticipants = new ArrayList<>();

    public List<ClassModel> getClasses() { return classes; }
    public void setClasses(List<ClassModel> classes) { this.classes = classes; }

    public void addClass(ClassModel classModel) {
        this.classes.add(classModel);
    }

    public List<FlowModel> getAllFlows() { return allFlows; }
    public void setAllFlows(List<FlowModel> allFlows) { this.allFlows = allFlows; }
    public void addFlow(FlowModel flow) { this.allFlows.add(flow); }

    public List<ParticipantModel> getAllParticipants() { return allParticipants; }
    public void setAllParticipants(List<ParticipantModel> allParticipants) { this.allParticipants = allParticipants; }
    public void addParticipant(ParticipantModel participant) { this.allParticipants.add(participant); }

    private List<EntityModel> entities = new ArrayList<>();

    public List<EntityModel> getEntities() { return entities; }
    public void setEntities(List<EntityModel> entities) { this.entities = entities; }
    public void addEntity(EntityModel entity) { this.entities.add(entity); }

    private List<EventModel> events = new ArrayList<>();

    public List<EventModel> getEvents() { return events; }
    public void setEvents(List<EventModel> events) { this.events = events; }
    public void addEvent(EventModel event) { this.events.add(event); }
}
