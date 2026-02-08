package org.aocdev.jdocusaurus.processor.config;

import javax.annotation.processing.ProcessingEnvironment;
import java.util.Map;

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
