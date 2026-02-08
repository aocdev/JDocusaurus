package org.aocdev.jdocusaurus.processor.scanner;

import org.aocdev.jdocusaurus.annotations.api.*;
import org.aocdev.jdocusaurus.annotations.config.*;
import org.aocdev.jdocusaurus.annotations.data.*;
import org.aocdev.jdocusaurus.annotations.event.*;
import org.aocdev.jdocusaurus.annotations.flow.*;
import org.aocdev.jdocusaurus.annotations.integration.JDocExternalService;
import org.aocdev.jdocusaurus.annotations.rule.*;
import org.aocdev.jdocusaurus.processor.model.*;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

public class AnnotationScanner {

    public ProjectModel scan(RoundEnvironment roundEnv) {
        ProjectModel project = new ProjectModel();

        for (Element element : roundEnv.getElementsAnnotatedWith(JDocClass.class)) {
            if (element.getKind() == ElementKind.CLASS || element.getKind() == ElementKind.INTERFACE) {
                TypeElement typeElement = (TypeElement) element;
                ClassModel classModel = scanClass(typeElement);
                project.addClass(classModel);
            }
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(JDocEntity.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                TypeElement typeElement = (TypeElement) element;
                EntityModel entityModel = scanEntity(typeElement);
                project.addEntity(entityModel);
            }
        }

        // Scan @JDocEvent
        for (Element element : roundEnv.getElementsAnnotatedWith(JDocEvent.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                TypeElement typeElement = (TypeElement) element;
                EventModel eventModel = scanEvent(typeElement);
                project.addEvent(eventModel);
            }
        }

        // Scan @JDocProduces on classes and methods
        scanProducers(roundEnv, project);

        // Scan @JDocConsumes on classes and methods
        scanConsumers(roundEnv, project);

        // Scan @JDocBusinessRule
        scanBusinessRules(roundEnv, project);

        // Scan @JDocExternalService
        scanExternalServices(roundEnv, project);

        // Scan @JDocConfig
        scanConfigs(roundEnv, project);

        return project;
    }

    private ClassModel scanClass(TypeElement typeElement) {
        JDocClass annotation = typeElement.getAnnotation(JDocClass.class);

        ClassModel classModel = new ClassModel();
        classModel.setClassName(typeElement.getSimpleName().toString());
        classModel.setQualifiedName(typeElement.getQualifiedName().toString());
        classModel.setName(annotation.name());
        classModel.setDescription(annotation.description());
        classModel.setBasePath(annotation.basePath());
        classModel.setVersion(annotation.version());
        classModel.setGroup(annotation.group());
        classModel.setTags(annotation.tags());

        // Scan @JDocParticipant
        JDocParticipant participantAnnotation = typeElement.getAnnotation(JDocParticipant.class);
        if (participantAnnotation != null) {
            ParticipantModel participant = new ParticipantModel();
            participant.setName(participantAnnotation.name());
            participant.setAlias(participantAnnotation.alias());
            participant.setType(participantAnnotation.type().name());
            classModel.setParticipant(participant);
        }

        // Scan @JDocFlow on class
        JDocFlow flowAnnotation = typeElement.getAnnotation(JDocFlow.class);
        if (flowAnnotation != null) {
            FlowModel flow = new FlowModel();
            flow.setName(flowAnnotation.name());
            flow.setDescription(flowAnnotation.description());
            flow.setTitle(flowAnnotation.title());
            classModel.getFlows().add(flow);
        }

        List<EndpointModel> endpoints = new ArrayList<>();
        for (Element enclosed : typeElement.getEnclosedElements()) {
            if (enclosed.getKind() == ElementKind.METHOD) {
                ExecutableElement method = (ExecutableElement) enclosed;
                JDocEndpoint endpointAnnotation = method.getAnnotation(JDocEndpoint.class);
                if (endpointAnnotation != null) {
                    endpoints.add(scanEndpoint(method, endpointAnnotation));
                }
            }
        }
        classModel.setEndpoints(endpoints);

        return classModel;
    }

    private EndpointModel scanEndpoint(ExecutableElement method, JDocEndpoint annotation) {
        EndpointModel endpoint = new EndpointModel();
        endpoint.setMethodName(method.getSimpleName().toString());
        endpoint.setHttpMethod(annotation.method().name());
        endpoint.setPath(annotation.path());
        endpoint.setDescription(annotation.description());
        endpoint.setSummary(annotation.summary());
        endpoint.setProduces(annotation.produces());
        endpoint.setConsumes(annotation.consumes());
        endpoint.setDeprecated(annotation.deprecated());
        endpoint.setDeprecatedMessage(annotation.deprecatedMessage());
        endpoint.setAuth(annotation.auth());

        endpoint.setParams(scanParams(method));
        endpoint.setResponses(scanResponses(method));
        endpoint.setHeaders(scanHeaders(method));
        endpoint.setManualSteps(scanFlowSteps(method));

        return endpoint;
    }

    private List<ParamModel> scanParams(ExecutableElement method) {
        List<ParamModel> params = new ArrayList<>();
        for (VariableElement param : method.getParameters()) {
            JDocParam annotation = param.getAnnotation(JDocParam.class);
            if (annotation != null) {
                ParamModel model = new ParamModel();
                String name = annotation.name();
                model.setName(name.isEmpty() ? param.getSimpleName().toString() : name);
                model.setDescription(annotation.description());
                model.setLocation(annotation.location().name());
                model.setRequired(annotation.required());
                model.setDefaultValue(annotation.defaultValue());
                model.setExample(annotation.example());
                String type = annotation.type();
                model.setType(type.isEmpty() ? param.asType().toString() : type);
                params.add(model);
            }
        }
        return params;
    }

    private List<ResponseModel> scanResponses(ExecutableElement method) {
        List<ResponseModel> responses = new ArrayList<>();

        JDocResponse single = method.getAnnotation(JDocResponse.class);
        JDocResponses multiple = method.getAnnotation(JDocResponses.class);

        JDocResponse[] annotations;
        if (multiple != null) {
            annotations = multiple.value();
        } else if (single != null) {
            annotations = new JDocResponse[]{single};
        } else {
            return responses;
        }

        for (JDocResponse annotation : annotations) {
            ResponseModel model = new ResponseModel();
            model.setCode(annotation.code());
            model.setDescription(annotation.description());
            model.setExample(annotation.example());
            try {
                model.setTypeName(annotation.type().getSimpleName());
            } catch (MirroredTypeException e) {
                TypeMirror typeMirror = e.getTypeMirror();
                String fullName = typeMirror.toString();
                int lastDot = fullName.lastIndexOf('.');
                model.setTypeName(lastDot >= 0 ? fullName.substring(lastDot + 1) : fullName);
            }
            responses.add(model);
        }

        return responses;
    }

    private List<HeaderModel> scanHeaders(ExecutableElement method) {
        List<HeaderModel> headers = new ArrayList<>();

        JDocHeader single = method.getAnnotation(JDocHeader.class);
        JDocHeaders multiple = method.getAnnotation(JDocHeaders.class);

        JDocHeader[] annotations;
        if (multiple != null) {
            annotations = multiple.value();
        } else if (single != null) {
            annotations = new JDocHeader[]{single};
        } else {
            return headers;
        }

        for (JDocHeader annotation : annotations) {
            HeaderModel model = new HeaderModel();
            model.setName(annotation.name());
            model.setDescription(annotation.description());
            model.setRequired(annotation.required());
            model.setDirection(annotation.direction().name());
            headers.add(model);
        }

        return headers;
    }

    private List<FlowStepModel> scanFlowSteps(ExecutableElement method) {
        List<FlowStepModel> steps = new ArrayList<>();

        JDocFlowStep single = method.getAnnotation(JDocFlowStep.class);
        JDocFlowSteps multiple = method.getAnnotation(JDocFlowSteps.class);

        JDocFlowStep[] annotations;
        if (multiple != null) {
            annotations = multiple.value();
        } else if (single != null) {
            annotations = new JDocFlowStep[]{single};
        } else {
            return steps;
        }

        for (JDocFlowStep annotation : annotations) {
            FlowStepModel model = new FlowStepModel();
            model.setFlow(annotation.flow());
            model.setOrder(annotation.order());
            model.setFrom(annotation.from());
            model.setTo(annotation.to());
            model.setMessage(annotation.message());
            model.setType(annotation.type().name());
            model.setReturnMessage(annotation.returnMessage());
            model.setCondition(annotation.condition());
            model.setNote(annotation.note());
            steps.add(model);
        }

        steps.sort((a, b) -> Integer.compare(a.getOrder(), b.getOrder()));
        return steps;
    }

    private EntityModel scanEntity(TypeElement typeElement) {
        JDocEntity annotation = typeElement.getAnnotation(JDocEntity.class);

        EntityModel entity = new EntityModel();
        entity.setClassName(typeElement.getSimpleName().toString());
        entity.setName(annotation.name());
        entity.setDescription(annotation.description());
        entity.setTableName(annotation.table());

        List<FieldModel> fields = new ArrayList<>();
        List<RelationModel> relations = new ArrayList<>();

        for (Element enclosed : typeElement.getEnclosedElements()) {
            if (enclosed.getKind() != ElementKind.FIELD) continue;
            VariableElement field = (VariableElement) enclosed;

            JDocField fieldAnnotation = field.getAnnotation(JDocField.class);
            if (fieldAnnotation != null) {
                FieldModel model = new FieldModel();
                model.setName(field.getSimpleName().toString());
                model.setTypeName(simplifyType(field.asType().toString()));
                model.setDescription(fieldAnnotation.description());
                model.setExample(fieldAnnotation.example());
                model.setNullable(fieldAnnotation.nullable());
                model.setConstraints(fieldAnnotation.constraints());
                fields.add(model);
            }

            JDocRelation relationAnnotation = field.getAnnotation(JDocRelation.class);
            if (relationAnnotation != null) {
                RelationModel model = new RelationModel();
                model.setFieldName(field.getSimpleName().toString());
                String target = relationAnnotation.target();
                model.setTargetEntityName(target.isEmpty() ? simplifyType(field.asType().toString()) : target);
                model.setType(relationAnnotation.type().name());
                model.setDescription(relationAnnotation.description());
                relations.add(model);
            }
        }

        entity.setFields(fields);
        entity.setRelations(relations);
        return entity;
    }

    private EventModel scanEvent(TypeElement typeElement) {
        JDocEvent annotation = typeElement.getAnnotation(JDocEvent.class);

        EventModel event = new EventModel();
        event.setClassName(typeElement.getSimpleName().toString());
        event.setName(annotation.name());
        event.setDescription(annotation.description());
        event.setTopic(annotation.topic());
        event.setSchema(annotation.schema());
        return event;
    }

    private void scanProducers(RoundEnvironment roundEnv, ProjectModel project) {
        for (Element element : roundEnv.getElementsAnnotatedWith(JDocProduces.class)) {
            scanProducerAnnotations(element, project, new JDocProduces[]{element.getAnnotation(JDocProduces.class)});
        }
        for (Element element : roundEnv.getElementsAnnotatedWith(JDocProducesAll.class)) {
            scanProducerAnnotations(element, project, element.getAnnotation(JDocProducesAll.class).value());
        }
    }

    private void scanProducerAnnotations(Element element, ProjectModel project, JDocProduces[] annotations) {
        String className = getEnclosingClassName(element);
        String methodName = element.getKind() == ElementKind.METHOD ? element.getSimpleName().toString() : "";

        for (JDocProduces annotation : annotations) {
            ProducerModel producer = new ProducerModel();
            producer.setClassName(className);
            producer.setMethodName(methodName);
            producer.setDescription(annotation.description());
            producer.setAsync(annotation.async());

            String eventName = resolveEventName(annotation);
            String topic = annotation.topic();
            producer.setTopic(topic);

            associateProducer(project, eventName, topic, producer);
        }
    }

    private void scanConsumers(RoundEnvironment roundEnv, ProjectModel project) {
        for (Element element : roundEnv.getElementsAnnotatedWith(JDocConsumes.class)) {
            scanConsumerAnnotations(element, project, new JDocConsumes[]{element.getAnnotation(JDocConsumes.class)});
        }
        for (Element element : roundEnv.getElementsAnnotatedWith(JDocConsumesAll.class)) {
            scanConsumerAnnotations(element, project, element.getAnnotation(JDocConsumesAll.class).value());
        }
    }

    private void scanConsumerAnnotations(Element element, ProjectModel project, JDocConsumes[] annotations) {
        String className = getEnclosingClassName(element);
        String methodName = element.getKind() == ElementKind.METHOD ? element.getSimpleName().toString() : "";

        for (JDocConsumes annotation : annotations) {
            ConsumerModel consumer = new ConsumerModel();
            consumer.setClassName(className);
            consumer.setMethodName(methodName);
            consumer.setDescription(annotation.description());
            consumer.setGroup(annotation.group());

            String eventName = resolveEventName(annotation);
            String topic = annotation.topic();
            consumer.setTopic(topic);

            associateConsumer(project, eventName, topic, consumer);
        }
    }

    private String resolveEventName(JDocProduces annotation) {
        try {
            return annotation.event().getSimpleName();
        } catch (MirroredTypeException e) {
            String fullName = e.getTypeMirror().toString();
            int lastDot = fullName.lastIndexOf('.');
            String name = lastDot >= 0 ? fullName.substring(lastDot + 1) : fullName;
            return "Void".equals(name) ? "" : name;
        }
    }

    private String resolveEventName(JDocConsumes annotation) {
        try {
            return annotation.event().getSimpleName();
        } catch (MirroredTypeException e) {
            String fullName = e.getTypeMirror().toString();
            int lastDot = fullName.lastIndexOf('.');
            String name = lastDot >= 0 ? fullName.substring(lastDot + 1) : fullName;
            return "Void".equals(name) ? "" : name;
        }
    }

    private void associateProducer(ProjectModel project, String eventName, String topic, ProducerModel producer) {
        for (EventModel event : project.getEvents()) {
            if (matchesEvent(event, eventName, topic)) {
                event.addProducer(producer);
                return;
            }
        }
        // Create orphan event if not found
        EventModel orphan = new EventModel();
        orphan.setClassName(eventName.isEmpty() ? topic : eventName);
        orphan.setName(eventName.isEmpty() ? topic : eventName);
        orphan.setTopic(topic);
        orphan.addProducer(producer);
        project.addEvent(orphan);
    }

    private void associateConsumer(ProjectModel project, String eventName, String topic, ConsumerModel consumer) {
        for (EventModel event : project.getEvents()) {
            if (matchesEvent(event, eventName, topic)) {
                event.addConsumer(consumer);
                return;
            }
        }
        EventModel orphan = new EventModel();
        orphan.setClassName(eventName.isEmpty() ? topic : eventName);
        orphan.setName(eventName.isEmpty() ? topic : eventName);
        orphan.setTopic(topic);
        orphan.addConsumer(consumer);
        project.addEvent(orphan);
    }

    private boolean matchesEvent(EventModel event, String eventName, String topic) {
        if (!eventName.isEmpty() && event.getClassName().equals(eventName)) return true;
        if (!topic.isEmpty() && topic.equals(event.getTopic())) return true;
        return false;
    }

    private String getEnclosingClassName(Element element) {
        if (element.getKind() == ElementKind.METHOD) {
            return element.getEnclosingElement().getSimpleName().toString();
        }
        return element.getSimpleName().toString();
    }

    private void scanBusinessRules(RoundEnvironment roundEnv, ProjectModel project) {
        for (Element element : roundEnv.getElementsAnnotatedWith(JDocBusinessRule.class)) {
            scanRuleAnnotations(element, project, new JDocBusinessRule[]{element.getAnnotation(JDocBusinessRule.class)});
        }
        for (Element element : roundEnv.getElementsAnnotatedWith(JDocBusinessRules.class)) {
            scanRuleAnnotations(element, project, element.getAnnotation(JDocBusinessRules.class).value());
        }
    }

    private void scanRuleAnnotations(Element element, ProjectModel project, JDocBusinessRule[] annotations) {
        String className = getEnclosingClassName(element);
        String methodName = element.getKind() == ElementKind.METHOD ? element.getSimpleName().toString() : "";

        for (JDocBusinessRule annotation : annotations) {
            BusinessRuleModel model = new BusinessRuleModel();
            model.setId(annotation.id());
            model.setRule(annotation.rule());
            model.setSeverity(annotation.severity().name());
            model.setRelatedRules(annotation.relatedRules());
            model.setAppliedInClass(className);
            model.setAppliedInMethod(methodName);
            project.addBusinessRule(model);
        }
    }

    private void scanExternalServices(RoundEnvironment roundEnv, ProjectModel project) {
        for (Element element : roundEnv.getElementsAnnotatedWith(JDocExternalService.class)) {
            JDocExternalService annotation = element.getAnnotation(JDocExternalService.class);
            ExternalServiceModel model = new ExternalServiceModel();
            model.setName(annotation.name());
            model.setDescription(annotation.description());
            model.setUrl(annotation.url());
            model.setType(annotation.type().name());
            model.setOwner(annotation.owner());

            if (element.getKind() == ElementKind.FIELD) {
                model.setUsedByClass(element.getEnclosingElement().getSimpleName().toString());
                model.setUsedByField(element.getSimpleName().toString());
            } else {
                model.setUsedByClass(element.getSimpleName().toString());
            }

            project.addExternalService(model);
        }
    }

    private void scanConfigs(RoundEnvironment roundEnv, ProjectModel project) {
        for (Element element : roundEnv.getElementsAnnotatedWith(JDocConfig.class)) {
            scanConfigAnnotations(element, project, new JDocConfig[]{element.getAnnotation(JDocConfig.class)});
        }
        for (Element element : roundEnv.getElementsAnnotatedWith(JDocConfigs.class)) {
            scanConfigAnnotations(element, project, element.getAnnotation(JDocConfigs.class).value());
        }
    }

    private void scanConfigAnnotations(Element element, ProjectModel project, JDocConfig[] annotations) {
        String className = getEnclosingClassName(element);

        for (JDocConfig annotation : annotations) {
            ConfigModel model = new ConfigModel();
            model.setKey(annotation.key());
            model.setDescription(annotation.description());
            model.setDefaultValue(annotation.defaultValue());
            model.setRequired(annotation.required());
            model.setSecret(annotation.secret());
            model.setExample(annotation.example());
            model.setDeclaredInClass(className);
            project.addConfig(model);
        }
    }

    private String simplifyType(String fullType) {
        if (fullType == null || fullType.isEmpty()) return "Object";
        int genericStart = fullType.indexOf('<');
        if (genericStart > 0) fullType = fullType.substring(0, genericStart);
        int lastDot = fullType.lastIndexOf('.');
        return lastDot >= 0 ? fullType.substring(lastDot + 1) : fullType;
    }
}
