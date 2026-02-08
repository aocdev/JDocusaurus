package org.aocdev.jdocusaurus.processor.scanner;

import org.aocdev.jdocusaurus.annotations.api.*;
import org.aocdev.jdocusaurus.annotations.data.*;
import org.aocdev.jdocusaurus.annotations.flow.*;
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

    public TypeElement getTypeElement(Element element) {
        return (TypeElement) element;
    }

    private String simplifyType(String fullType) {
        if (fullType == null || fullType.isEmpty()) return "Object";
        int genericStart = fullType.indexOf('<');
        if (genericStart > 0) fullType = fullType.substring(0, genericStart);
        int lastDot = fullType.lastIndexOf('.');
        return lastDot >= 0 ? fullType.substring(lastDot + 1) : fullType;
    }
}
