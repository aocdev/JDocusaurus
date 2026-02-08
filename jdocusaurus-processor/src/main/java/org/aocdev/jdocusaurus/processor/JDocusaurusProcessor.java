package org.aocdev.jdocusaurus.processor;

import org.aocdev.jdocusaurus.processor.config.JDocusaurusConfig;
import org.aocdev.jdocusaurus.processor.generator.*;
import org.aocdev.jdocusaurus.processor.model.*;
import org.aocdev.jdocusaurus.processor.scanner.AnnotationScanner;
import org.aocdev.jdocusaurus.processor.scanner.JavaParserScanner;
import org.aocdev.jdocusaurus.processor.scanner.JpaScanner;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.*;

@SupportedAnnotationTypes({
        "org.aocdev.jdocusaurus.annotations.api.JDocClass",
        "org.aocdev.jdocusaurus.annotations.api.JDocEndpoint",
        "org.aocdev.jdocusaurus.annotations.api.JDocParam",
        "org.aocdev.jdocusaurus.annotations.api.JDocResponse",
        "org.aocdev.jdocusaurus.annotations.api.JDocResponses",
        "org.aocdev.jdocusaurus.annotations.api.JDocHeader",
        "org.aocdev.jdocusaurus.annotations.api.JDocHeaders",
        "org.aocdev.jdocusaurus.annotations.flow.JDocFlow",
        "org.aocdev.jdocusaurus.annotations.flow.JDocFlowStep",
        "org.aocdev.jdocusaurus.annotations.flow.JDocFlowSteps",
        "org.aocdev.jdocusaurus.annotations.flow.JDocParticipant",
        "org.aocdev.jdocusaurus.annotations.data.JDocEntity",
        "org.aocdev.jdocusaurus.annotations.data.JDocField",
        "org.aocdev.jdocusaurus.annotations.data.JDocRelation",
        "org.aocdev.jdocusaurus.annotations.event.JDocEvent",
        "org.aocdev.jdocusaurus.annotations.event.JDocProduces",
        "org.aocdev.jdocusaurus.annotations.event.JDocProducesAll",
        "org.aocdev.jdocusaurus.annotations.event.JDocConsumes",
        "org.aocdev.jdocusaurus.annotations.event.JDocConsumesAll",
        "org.aocdev.jdocusaurus.annotations.rule.JDocBusinessRule",
        "org.aocdev.jdocusaurus.annotations.rule.JDocBusinessRules",
        "org.aocdev.jdocusaurus.annotations.integration.JDocExternalService",
        "org.aocdev.jdocusaurus.annotations.config.JDocConfig",
        "org.aocdev.jdocusaurus.annotations.config.JDocConfigs"
})
@SupportedOptions({
        "jdoc.outputDir",
        "jdoc.fullStructure",
        "jdoc.projectName",
        "jdoc.projectDescription",
        "jdoc.autoFlowDepth",
        "jdoc.autoFlowEnabled"
})
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class JDocusaurusProcessor extends AbstractProcessor {

    private boolean processed = false;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (processed || roundEnv.processingOver()) {
            return false;
        }

        JDocusaurusConfig config = JDocusaurusConfig.fromProcessingEnvironment(processingEnv);

        AnnotationScanner scanner = new AnnotationScanner();
        ProjectModel model = scanner.scan(roundEnv);

        if (model.getClasses().isEmpty() && model.getEntities().isEmpty() && model.getEvents().isEmpty()
                && model.getBusinessRules().isEmpty() && model.getExternalServices().isEmpty()
                && model.getConfigs().isEmpty()) {
            return false;
        }

        // Enrich entities with JPA data
        enrichEntitiesWithJpa(model, roundEnv);

        // Collect all participants from classes
        for (ClassModel classModel : model.getClasses()) {
            if (classModel.getParticipant() != null) {
                model.addParticipant(classModel.getParticipant());
            }
        }

        // Collect manual flow steps and group by flow name
        collectFlows(model);

        // Run JavaParser auto-detection for call graphs
        if (config.isAutoFlowEnabled()) {
            runCallGraphAnalysis(model, config.getAutoFlowDepth());
        }

        String outputDir = config.getOutputDir();

        try {
            Filer filer = processingEnv.getFiler();

            // Content generators
            new MarkdownGenerator(outputDir).generate(model, filer);
            new FlowMarkdownGenerator(outputDir).generate(model, filer);
            new EntityMarkdownGenerator(outputDir).generate(model, filer);
            new EventMarkdownGenerator(outputDir).generate(model, filer);
            new RuleMarkdownGenerator(outputDir).generate(model, filer);

            String serviceName = config.getProjectName().isEmpty()
                    ? (model.getClasses().isEmpty() ? "MyService" : model.getClasses().get(0).getName())
                    : config.getProjectName();
            new DependencyMapGenerator(serviceName, outputDir).generate(model, filer);
            new ConfigMarkdownGenerator(outputDir).generate(model, filer);

            // Structure generators
            new IndexGenerator(config).generate(model, filer);
            new SidebarGenerator(config).generate(model, filer);

            int endpointCount = model.getClasses().stream()
                    .mapToInt(c -> c.getEndpoints().size())
                    .sum();
            int flowCount = model.getAllFlows().size();
            int diagramCount = (int) model.getClasses().stream()
                    .flatMap(c -> c.getEndpoints().stream())
                    .filter(EndpointModel::hasFlowDiagram)
                    .count();
            int entityCount = model.getEntities().size();
            int eventCount = model.getEvents().size();
            int ruleCount = model.getBusinessRules().size();
            int serviceCount = model.getExternalServices().size();
            int configCount = model.getConfigs().size();

            // index.md + api/index.md + flows/index.md (if present)
            int indexFiles = 1
                    + (model.getClasses().isEmpty() ? 0 : 1)
                    + (flowCount > 0 ? 1 : 0);
            int sidebarFile = config.isFullStructure() ? 1 : 0;

            int fileCount = indexFiles + sidebarFile
                    + model.getClasses().size() + flowCount
                    + (entityCount > 0 ? entityCount + 1 : 0)
                    + (eventCount > 0 ? eventCount + 1 : 0)
                    + (ruleCount > 0 ? 1 : 0)
                    + (serviceCount > 0 ? 1 : 0)
                    + (configCount > 0 ? 1 : 0);

            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.NOTE,
                    String.format("JDocusaurus: generados %d ficheros (%d clases, %d endpoints, %d diagramas, %d flujos, %d entidades, %d eventos, %d reglas, %d integraciones, %d configs)",
                            fileCount,
                            model.getClasses().size(),
                            endpointCount,
                            diagramCount,
                            flowCount,
                            entityCount,
                            eventCount,
                            ruleCount,
                            serviceCount,
                            configCount)
            );
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.ERROR,
                    "JDocusaurus: error generando documentacion - " + e.getMessage()
            );
        }

        processed = true;
        return true;
    }

    private void collectFlows(ProjectModel model) {
        Map<String, FlowModel> flowMap = new LinkedHashMap<>();

        // Collect flows declared on classes
        for (ClassModel classModel : model.getClasses()) {
            for (FlowModel flow : classModel.getFlows()) {
                flowMap.putIfAbsent(flow.getName(), flow);
            }
        }

        // Collect steps from endpoints and assign to flows
        for (ClassModel classModel : model.getClasses()) {
            for (EndpointModel endpoint : classModel.getEndpoints()) {
                for (FlowStepModel step : endpoint.getManualSteps()) {
                    String flowName = step.getFlow();
                    if (flowName != null && !flowName.isEmpty()) {
                        FlowModel flow = flowMap.computeIfAbsent(flowName, name -> {
                            FlowModel f = new FlowModel();
                            f.setName(name);
                            f.setTitle(name);
                            f.setDescription("");
                            return f;
                        });
                        flow.addStep(step);
                    }
                }
            }
        }

        // Sort steps within each flow
        for (FlowModel flow : flowMap.values()) {
            flow.getSteps().sort(Comparator.comparingInt(FlowStepModel::getOrder));
        }

        flowMap.values().stream()
                .filter(f -> !f.getSteps().isEmpty())
                .forEach(model::addFlow);
    }

    private void enrichEntitiesWithJpa(ProjectModel model, RoundEnvironment roundEnv) {
        if (model.getEntities().isEmpty()) return;

        JpaScanner jpaScanner = new JpaScanner();
        for (Element element : roundEnv.getElementsAnnotatedWith(
                org.aocdev.jdocusaurus.annotations.data.JDocEntity.class)) {
            if (element.getKind() == javax.lang.model.element.ElementKind.CLASS) {
                javax.lang.model.element.TypeElement typeElement =
                        (javax.lang.model.element.TypeElement) element;
                String className = typeElement.getSimpleName().toString();

                for (EntityModel entity : model.getEntities()) {
                    if (entity.getClassName().equals(className)) {
                        jpaScanner.enrichEntity(entity, typeElement);
                        break;
                    }
                }
            }
        }
    }

    private void runCallGraphAnalysis(ProjectModel model, int maxDepth) {
        try {
            JavaParserScanner javaParserScanner = new JavaParserScanner(processingEnv);

            for (ClassModel classModel : model.getClasses()) {
                for (EndpointModel endpoint : classModel.getEndpoints()) {
                    if (!endpoint.getManualSteps().isEmpty()) continue;

                    CallGraphModel graph = javaParserScanner.analyzeMethod(
                            classModel.getQualifiedName(), endpoint.getMethodName());
                    if (graph != null && !graph.isEmpty()) {
                        endpoint.setCallGraph(graph);
                    }
                }
            }
        } catch (Exception e) {
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.NOTE,
                    "JDocusaurus: analisis de call graph omitido - " + e.getMessage()
            );
        }
    }
}
