package org.aocdev.jdocusaurus.processor.model;

/**
 * Model representing an event producer (a class/method that emits an {@link EventModel}).
 *
 * @since 1.0.0
 */
public class ProducerModel {
    private String className;
    private String methodName;
    private String topic;
    private String description;
    private boolean async;

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getMethodName() { return methodName; }
    public void setMethodName(String methodName) { this.methodName = methodName; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isAsync() { return async; }
    public void setAsync(boolean async) { this.async = async; }

    public String getLocation() {
        if (methodName != null && !methodName.isEmpty()) {
            return className + "." + methodName + "()";
        }
        return className;
    }
}
