package org.aocdev.jdocusaurus.processor.generator;

import org.aocdev.jdocusaurus.processor.model.ProjectModel;

import javax.annotation.processing.Filer;
import java.io.IOException;

public interface Generator {
    void generate(ProjectModel model, Filer filer) throws IOException;
}
