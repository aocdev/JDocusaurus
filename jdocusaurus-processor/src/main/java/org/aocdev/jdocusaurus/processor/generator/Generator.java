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
