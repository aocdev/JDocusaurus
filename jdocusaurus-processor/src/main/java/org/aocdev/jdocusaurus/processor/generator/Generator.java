package org.aocdev.jdocusaurus.processor.generator;

import org.aocdev.jdocusaurus.processor.model.ProjectModel;

import java.io.IOException;

public interface Generator {
    void generate(ProjectModel model, DocWriter writer) throws IOException;
}
