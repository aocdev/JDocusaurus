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
 * Model representing a relationship between two entities in the ER diagram.
 *
 * <p>Provides {@code getMermaidRelation()} to convert the cardinality
 * to Mermaid ER diagram notation.
 *
 * @since 1.0.0
 */
public class RelationModel {
    private String fieldName;
    private String targetEntityName;
    private String type;
    private String description;

    public String getFieldName() { return fieldName; }
    public void setFieldName(String fieldName) { this.fieldName = fieldName; }

    public String getTargetEntityName() { return targetEntityName; }
    public void setTargetEntityName(String targetEntityName) { this.targetEntityName = targetEntityName; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getMermaidRelation() {
        return switch (type) {
            case "ONE_TO_ONE" -> "||--||";
            case "ONE_TO_MANY" -> "||--o{";
            case "MANY_TO_ONE" -> "}o--||";
            case "MANY_TO_MANY" -> "}o--o{";
            default -> "||--||";
        };
    }
}
