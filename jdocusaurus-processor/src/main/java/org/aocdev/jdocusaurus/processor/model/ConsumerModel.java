/*
 * Copyright 2026 aocdev (Albert Ortells)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.aocdev.jdocusaurus.processor.model;

/**
 * Model representing an event consumer (a class/method that listens to an {@link EventModel}).
 *
 * @since 1.0.0
 */
public class ConsumerModel {
    private String className;
    private String methodName;
    private String topic;
    private String description;
    private String group;

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getMethodName() { return methodName; }
    public void setMethodName(String methodName) { this.methodName = methodName; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; }

    public String getLocation() {
        if (methodName != null && !methodName.isEmpty()) {
            return className + "." + methodName + "()";
        }
        return className;
    }
}
