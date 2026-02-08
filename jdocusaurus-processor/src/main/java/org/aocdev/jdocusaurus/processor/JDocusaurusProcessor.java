package org.aocdev.jdocusaurus.processor;

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
import java.util.stream.Collectors;

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
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class JDocusaurusProcessor extends AbstractProcessor {

    private boolean processed = false;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (processed || roundEnv.processingOver()) {
            return false;
        }

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
        runCallGraphAnalysis(model);

        try {
            MarkdownGenerator markdownGenerator = new MarkdownGenerator();
            markdownGenerator.generate(model, processingEnv.getFiler());

            FlowMarkdownGenerator flowGenerator = new FlowMarkdownGenerator();
            flowGenerator.generate(model, processingEnv.getFiler());

            EntityMarkdownGenerator entityGenerator = new EntityMarkdownGenerator();
            entityGenerator.generate(model, processingEnv.getFiler());

            EventMarkdownGenerator eventGenerator = new EventMarkdownGenerator();
            eventGenerator.generate(model, processingEnv.getFiler());

            RuleMarkdownGenerator ruleGenerator = new RuleMarkdownGenerator();
            ruleGenerator.generate(model, processingEnv.getFiler());

            String serviceName = model.getClasses().isEmpty()
                    ? "MyService"
                    : model.getClasses().get(0).getName();
            DependencyMapGenerator depGenerator = new DependencyMapGenerator(serviceName);
            depGenerator.generate(model, processingEnv.getFiler());

            ConfigMarkdownGenerator configGenerator = new ConfigMarkdownGenerator();
            configGenerator.generate(model, processingEnv.getFiler());

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

            int fileCount = model.getClasses().size() + flowCount
                    + (entityCount > 0 ? entityCount + 1 : 0)
                    + (eventCount > 0 ? eventCount + 1 : 0)
                    + (ruleCount > 0 ? 1 : 0)
                    + (serviceCount > 0 ? 1 : 0)
                    + (configCount > 0 ? 1 : 0);

            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.NOTE,
                    String.format("JDocusaurus: generados %d ficheros .md (%d clases, %d endpoints, %d diagramas, %d flujos, %d entidades, %d eventos, %d reglas, %d integraciones, %d configs)",
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

    private void runCallGraphAnalysis(ProjectModel model) {
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
