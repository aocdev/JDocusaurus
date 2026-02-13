package org.aocdev.jdocusaurus.processor.model;

/**
 * Model representing a single step within a {@link FlowModel} sequence diagram.
 *
 * @since 1.0.0
 */
public class FlowStepModel {
    private String flow;
    private int order;
    private String from;
    private String to;
    private String message;
    private String type;
    private String returnMessage;
    private String condition;
    private String note;

    public String getFlow() { return flow; }
    public void setFlow(String flow) { this.flow = flow; }

    public int getOrder() { return order; }
    public void setOrder(int order) { this.order = order; }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getReturnMessage() { return returnMessage; }
    public void setReturnMessage(String returnMessage) { this.returnMessage = returnMessage; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
