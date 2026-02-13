package org.aocdev.jdocusaurus.processor.generator;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * File output abstraction that supports both relative and absolute paths.
 *
 * <p>When {@code outputDir} is an absolute path (e.g., {@code /home/user/docs}),
 * files are written directly via {@link java.nio.file.Files}. When relative
 * (e.g., {@code "docs"}), files are written via the annotation processor's
 * {@link javax.annotation.processing.Filer} API to {@code target/classes/}.
 *
 * @since 1.0.0
 */
public class DocWriter {

    private final String outputDir;
    private final Filer filer;
    private final boolean absolute;

    public DocWriter(String outputDir, Filer filer) {
        this.outputDir = outputDir;
        this.filer = filer;
        this.absolute = Path.of(outputDir).isAbsolute();
    }

    /**
     * Writes content to a file at the given path relative to the output directory.
     *
     * @param relativePath path relative to outputDir (e.g., {@code "api/user-controller.md"})
     * @param content      file content to write
     * @throws IOException if the file cannot be created or written
     */
    public void write(String relativePath, String content) throws IOException {
        if (absolute) {
            Path path = Path.of(outputDir, relativePath);
            Files.createDirectories(path.getParent());
            Files.writeString(path, content);
        } else {
            FileObject file = filer.createResource(
                    StandardLocation.CLASS_OUTPUT, "", outputDir + "/" + relativePath);
            try (Writer writer = file.openWriter()) {
                writer.write(content);
            }
        }
    }

    /** Returns {@code true} if the output directory is an absolute path. */
    public boolean isAbsolute() {
        return absolute;
    }

    /** Returns the configured output directory. */
    public String getOutputDir() {
        return outputDir;
    }
}
