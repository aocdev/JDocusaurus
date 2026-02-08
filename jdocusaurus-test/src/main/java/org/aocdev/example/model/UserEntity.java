package org.aocdev.example.model;

import org.aocdev.jdocusaurus.annotations.data.JDocEntity;
import org.aocdev.jdocusaurus.annotations.data.JDocField;
import org.aocdev.jdocusaurus.annotations.data.JDocRelation;
import org.aocdev.jdocusaurus.annotations.enums.RelationType;

import java.util.List;

@JDocEntity(
        name = "Usuario",
        description = "Entidad que representa un usuario registrado en el sistema",
        table = "users"
)
public class UserEntity {

    @JDocField(description = "Identificador unico del usuario", nullable = false, constraints = "PK, AUTO_INCREMENT")
    private Long id;

    @JDocField(description = "Nombre completo del usuario", example = "Juan Garcia", nullable = false)
    private String name;

    @JDocField(description = "Direccion de email", example = "juan@example.com", nullable = false, constraints = "UNIQUE")
    private String email;

    @JDocField(description = "Hash de la contrasena", nullable = false)
    private String passwordHash;

    @JDocField(description = "Estado del usuario: ACTIVE, INACTIVE, BLOCKED", example = "ACTIVE", nullable = false)
    private String status;

    @JDocRelation(target = "Address", type = RelationType.ONE_TO_MANY, description = "Direcciones del usuario")
    private List<Object> addresses;

    @JDocRelation(target = "Order", type = RelationType.ONE_TO_MANY, description = "Pedidos realizados por el usuario")
    private List<Object> orders;
}
