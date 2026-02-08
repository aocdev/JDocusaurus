package org.aocdev.jdocusaurus.processor.model;

import java.util.ArrayList;
import java.util.List;

public class FlowModel {
    private String name;
    private String description;
    private String title;
    private List<FlowStepModel> steps = new ArrayList<>();

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDisplayTitle() {
        return (title != null && !title.isEmpty()) ? title : name;
    }

    public List<FlowStepModel> getSteps() { return steps; }
    public void setSteps(List<FlowStepModel> steps) { this.steps = steps; }

    public void addStep(FlowStepModel step) { this.steps.add(step); }
}
