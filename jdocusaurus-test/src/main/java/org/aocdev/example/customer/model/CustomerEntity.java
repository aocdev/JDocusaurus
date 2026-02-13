package org.aocdev.example.customer.model;

import org.aocdev.jdocusaurus.annotations.data.JDocEntity;
import org.aocdev.jdocusaurus.annotations.data.JDocField;
import org.aocdev.jdocusaurus.annotations.data.JDocRelation;
import org.aocdev.jdocusaurus.annotations.enums.RelationType;

import java.util.List;

@JDocEntity(
        name = "Cliente",
        description = "Entidad que representa un cliente registrado en el sistema",
        table = "customers"
)
public class CustomerEntity {

    @JDocField(description = "Identificador unico del cliente", nullable = false, constraints = "PK, AUTO_INCREMENT")
    private Long id;

    @JDocField(description = "Nombre completo del cliente", example = "Juan Garcia", nullable = false)
    private String name;

    @JDocField(description = "Direccion de email", example = "juan@example.com", nullable = false, constraints = "UNIQUE")
    private String email;

    @JDocField(description = "Hash de la contrasena", nullable = false)
    private String passwordHash;

    @JDocField(description = "Estado del cliente: ACTIVE, INACTIVE, BLOCKED", example = "ACTIVE", nullable = false)
    private String status;

    @JDocRelation(target = "Direccion", type = RelationType.ONE_TO_MANY, description = "Direcciones del cliente")
    private List<Object> addresses;

    @JDocRelation(target = "Pedido", type = RelationType.ONE_TO_MANY, description = "Pedidos realizados por el cliente")
    private List<Object> orders;
}
