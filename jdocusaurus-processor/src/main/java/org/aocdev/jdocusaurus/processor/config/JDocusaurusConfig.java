package org.aocdev.jdocusaurus.processor.config;

import javax.annotation.processing.ProcessingEnvironment;
import java.util.Map;

/**
 * Configuration holder for the JDocusaurus annotation processor.
 *
 * <p>Reads options from the compiler environment via {@code -Ajdoc.*}
 * arguments passed to {@code maven-compiler-plugin}.
 *
 * <p><b>Available options:</b>
 * <ul>
 *   <li>{@code jdoc.outputDir} - Output directory (default: {@code "docs"})</li>
 *   <li>{@code jdoc.fullStructure} - Generate sidebars.js (default: false)</li>
 *   <li>{@code jdoc.projectName} - Project name for the index page</li>
 *   <li>{@code jdoc.projectDescription} - Project description</li>
 *   <li>{@code jdoc.autoFlowDepth} - JavaParser recursion depth (default: 5)</li>
 *   <li>{@code jdoc.autoFlowEnabled} - Enable auto flow detection (default: true)</li>
 *   <li>{@code jdoc.sourcePath} - Semicolon-separated source directories for JavaParser analysis</li>
 * </ul>
 *
 * @since 1.0.0
 */
public class JDocusaurusConfig {

    private String outputDir = "docs";
    private boolean fullStructure = false;
    private String projectName = "";
    private String projectDescription = "";
    private int autoFlowDepth = 5;
    private boolean autoFlowEnabled = true;

    public static JDocusaurusConfig fromProcessingEnvironment(ProcessingEnvironment env) {
        JDocusaurusConfig config = new JDocusaurusConfig();
        Map<String, String> options = env.getOptions();

        if (options.containsKey("jdoc.outputDir")) {
            config.outputDir = options.get("jdoc.outputDir");
        }
        if (options.containsKey("jdoc.fullStructure")) {
            config.fullStructure = Boolean.parseBoolean(options.get("jdoc.fullStructure"));
        }
        if (options.containsKey("jdoc.projectName")) {
            config.projectName = options.get("jdoc.projectName");
        }
        if (options.containsKey("jdoc.projectDescription")) {
            config.projectDescription = options.get("jdoc.projectDescription");
        }
        if (options.containsKey("jdoc.autoFlowDepth")) {
            try {
                config.autoFlowDepth = Integer.parseInt(options.get("jdoc.autoFlowDepth"));
            } catch (NumberFormatException ignored) {}
        }
        if (options.containsKey("jdoc.autoFlowEnabled")) {
            config.autoFlowEnabled = Boolean.parseBoolean(options.get("jdoc.autoFlowEnabled"));
        }

        return config;
    }

    public String getOutputDir() { return outputDir; }
    public boolean isFullStructure() { return fullStructure; }
    public String getProjectName() { return projectName; }
    public String getProjectDescription() { return projectDescription; }
    public int getAutoFlowDepth() { return autoFlowDepth; }
    public boolean isAutoFlowEnabled() { return autoFlowEnabled; }
}
