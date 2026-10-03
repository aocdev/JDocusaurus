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
package org.aocdev.jdocusaurus.processor.generator;

import org.aocdev.jdocusaurus.processor.model.ProjectModel;

import java.io.IOException;

/**
 * Contract for all documentation generators.
 *
 * <p>Each implementation produces one or more Markdown/JS files from
 * the {@link ProjectModel} using the {@link DocWriter} abstraction.
 *
 * @since 1.0.0
 */
public interface Generator {
    /**
     * Generates documentation files from the project model.
     *
     * @param model  the aggregated project documentation model
     * @param writer the file writer abstraction (supports absolute/relative paths)
     * @throws IOException if a file cannot be written
     */
    void generate(ProjectModel model, DocWriter writer) throws IOException;
}
