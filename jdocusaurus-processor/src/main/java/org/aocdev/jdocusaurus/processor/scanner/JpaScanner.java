package org.aocdev.jdocusaurus.processor.scanner;

import org.aocdev.jdocusaurus.processor.model.EntityModel;
import org.aocdev.jdocusaurus.processor.model.FieldModel;
import org.aocdev.jdocusaurus.processor.model.RelationModel;

import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.*;

public class JpaScanner {

    private static final Set<String> JPA_ID_ANNOTATIONS = Set.of(
            "javax.persistence.Id",
            "jakarta.persistence.Id"
    );

    private static final Set<String> JPA_COLUMN_ANNOTATIONS = Set.of(
            "javax.persistence.Column",
            "jakarta.persistence.Column"
    );

    private static final Map<String, String> JPA_RELATION_ANNOTATIONS = Map.of(
            "javax.persistence.OneToOne", "ONE_TO_ONE",
            "jakarta.persistence.OneToOne", "ONE_TO_ONE",
            "javax.persistence.OneToMany", "ONE_TO_MANY",
            "jakarta.persistence.OneToMany", "ONE_TO_MANY",
            "javax.persistence.ManyToOne", "MANY_TO_ONE",
            "jakarta.persistence.ManyToOne", "MANY_TO_ONE",
            "javax.persistence.ManyToMany", "MANY_TO_MANY",
            "jakarta.persistence.ManyToMany", "MANY_TO_MANY"
    );

    private static final Set<String> JPA_TABLE_ANNOTATIONS = Set.of(
            "javax.persistence.Table",
            "jakarta.persistence.Table"
    );

    public void enrichEntity(EntityModel entity, TypeElement classElement) {
        // Try to read table name from JPA @Table
        if (entity.getTableName() == null || entity.getTableName().isEmpty()) {
            String tableName = findJpaTableName(classElement);
            if (tableName != null) {
                entity.setTableName(tableName);
            }
        }

        Set<String> documentedFields = new HashSet<>();
        for (FieldModel f : entity.getFields()) {
            documentedFields.add(f.getName());
        }

        Set<String> documentedRelations = new HashSet<>();
        for (RelationModel r : entity.getRelations()) {
            documentedRelations.add(r.getFieldName());
        }

        for (Element enclosed : classElement.getEnclosedElements()) {
            if (enclosed.getKind() != ElementKind.FIELD) continue;
            VariableElement field = (VariableElement) enclosed;
            String fieldName = field.getSimpleName().toString();

            // Enrich existing field or create new one from JPA
            if (!documentedFields.contains(fieldName)) {
                FieldModel fieldModel = buildFieldFromJpa(field);
                if (fieldModel != null) {
                    entity.getFields().add(fieldModel);
                    documentedFields.add(fieldName);
                }
            } else {
                enrichExistingField(entity, field);
            }

            // Enrich relations from JPA
            if (!documentedRelations.contains(fieldName)) {
                RelationModel relation = buildRelationFromJpa(field);
                if (relation != null) {
                    entity.getRelations().add(relation);
                    documentedRelations.add(fieldName);
                }
            }
        }
    }

    private FieldModel buildFieldFromJpa(VariableElement field) {
        boolean isId = hasAnyAnnotation(field, JPA_ID_ANNOTATIONS);
        AnnotationMirror columnMirror = findAnnotation(field, JPA_COLUMN_ANNOTATIONS);

        if (!isId && columnMirror == null) return null;

        FieldModel model = new FieldModel();
        model.setName(field.getSimpleName().toString());
        model.setTypeName(simplifyType(field.asType().toString()));
        model.setPrimaryKey(isId);

        if (columnMirror != null) {
            Map<String, Object> values = extractAnnotationValues(columnMirror);
            Object nullable = values.get("nullable");
            if (nullable != null) {
                model.setNullable((Boolean) nullable);
            }
            Object name = values.get("name");
            if (name != null && !name.toString().isEmpty()) {
                model.setDescription("Column: " + name);
            }
        }

        if (isId) {
            model.setNullable(false);
            model.setConstraints("PK");
        }

        return model;
    }

    private void enrichExistingField(EntityModel entity, VariableElement field) {
        String fieldName = field.getSimpleName().toString();
        boolean isId = hasAnyAnnotation(field, JPA_ID_ANNOTATIONS);

        if (isId) {
            for (FieldModel f : entity.getFields()) {
                if (f.getName().equals(fieldName)) {
                    f.setPrimaryKey(true);
                    if (f.getConstraints() == null || f.getConstraints().isEmpty()) {
                        f.setConstraints("PK");
                    }
                    break;
                }
            }
        }
    }

    private RelationModel buildRelationFromJpa(VariableElement field) {
        for (Map.Entry<String, String> entry : JPA_RELATION_ANNOTATIONS.entrySet()) {
            AnnotationMirror mirror = findAnnotationByName(field, entry.getKey());
            if (mirror != null) {
                RelationModel relation = new RelationModel();
                relation.setFieldName(field.getSimpleName().toString());
                relation.setType(entry.getValue());
                relation.setTargetEntityName(resolveTargetEntity(field, mirror));
                return relation;
            }
        }
        return null;
    }

    private String resolveTargetEntity(VariableElement field, AnnotationMirror mirror) {
        // Try to get targetEntity from annotation
        Map<String, Object> values = extractAnnotationValues(mirror);
        Object targetEntity = values.get("targetEntity");
        if (targetEntity != null && !targetEntity.toString().equals("void")) {
            String full = targetEntity.toString();
            int lastDot = full.lastIndexOf('.');
            return lastDot >= 0 ? full.substring(lastDot + 1) : full;
        }

        // Infer from field type (handle Collection<Entity>)
        TypeMirror typeMirror = field.asType();
        String typeName = typeMirror.toString();

        // Handle generic types like List<Order>, Set<Address>
        if (typeMirror instanceof DeclaredType declaredType) {
            List<? extends TypeMirror> typeArgs = declaredType.getTypeArguments();
            if (!typeArgs.isEmpty()) {
                typeName = typeArgs.get(0).toString();
            }
        }

        int lastDot = typeName.lastIndexOf('.');
        return lastDot >= 0 ? typeName.substring(lastDot + 1) : typeName;
    }

    private String findJpaTableName(TypeElement classElement) {
        AnnotationMirror tableMirror = findAnnotation(classElement, JPA_TABLE_ANNOTATIONS);
        if (tableMirror != null) {
            Map<String, Object> values = extractAnnotationValues(tableMirror);
            Object name = values.get("name");
            if (name != null && !name.toString().isEmpty()) {
                return name.toString();
            }
        }
        return null;
    }

    private boolean hasAnyAnnotation(Element element, Set<String> annotationNames) {
        for (AnnotationMirror mirror : element.getAnnotationMirrors()) {
            String name = mirror.getAnnotationType().toString();
            if (annotationNames.contains(name)) return true;
        }
        return false;
    }

    private AnnotationMirror findAnnotation(Element element, Set<String> annotationNames) {
        for (AnnotationMirror mirror : element.getAnnotationMirrors()) {
            String name = mirror.getAnnotationType().toString();
            if (annotationNames.contains(name)) return mirror;
        }
        return null;
    }

    private AnnotationMirror findAnnotationByName(Element element, String annotationName) {
        for (AnnotationMirror mirror : element.getAnnotationMirrors()) {
            if (mirror.getAnnotationType().toString().equals(annotationName)) {
                return mirror;
            }
        }
        return null;
    }

    private Map<String, Object> extractAnnotationValues(AnnotationMirror mirror) {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
                mirror.getElementValues().entrySet()) {
            result.put(entry.getKey().getSimpleName().toString(), entry.getValue().getValue());
        }
        return result;
    }

    private String simplifyType(String fullType) {
        if (fullType == null || fullType.isEmpty()) return "Object";
        // Remove generics
        int genericStart = fullType.indexOf('<');
        if (genericStart > 0) fullType = fullType.substring(0, genericStart);
        int lastDot = fullType.lastIndexOf('.');
        return lastDot >= 0 ? fullType.substring(lastDot + 1) : fullType;
    }
}
