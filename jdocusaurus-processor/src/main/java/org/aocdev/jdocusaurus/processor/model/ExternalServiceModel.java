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
 * Model representing a {@code @JDocExternalService}-annotated external dependency.
 *
 * <p>Used to generate the Mermaid dependency map and the integrations table.
 *
 * @since 1.0.0
 */
public class ExternalServiceModel {
    private String name;
    private String description;
    private String url;
    private String type;
    private String owner;
    private String usedByClass;
    private String usedByField;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public String getUsedByClass() { return usedByClass; }
    public void setUsedByClass(String usedByClass) { this.usedByClass = usedByClass; }

    public String getUsedByField() { return usedByField; }
    public void setUsedByField(String usedByField) { this.usedByField = usedByField; }
}
