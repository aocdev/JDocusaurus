package org.aocdev.jdocusaurus.processor.generator;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Abstraccion para escritura de ficheros generados.
 * <p>
 * Si outputDir es una ruta absoluta (ej: /home/user/docs), escribe directamente
 * en el sistema de ficheros con java.nio.file.Files.
 * <p>
 * Si outputDir es una ruta relativa (ej: docs), escribe via la API Filer del
 * annotation processor (dentro de target/classes/).
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
     * Escribe contenido en un fichero relativo al outputDir.
     *
     * @param relativePath ruta relativa dentro del outputDir (ej: "api/user-controller.md")
     * @param content      contenido del fichero
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

    public boolean isAbsolute() {
        return absolute;
    }

    public String getOutputDir() {
        return outputDir;
    }
}
