package org.aocdev.example.order.model;

import org.aocdev.jdocusaurus.annotations.data.JDocEntity;
import org.aocdev.jdocusaurus.annotations.data.JDocField;
import org.aocdev.jdocusaurus.annotations.data.JDocRelation;
import org.aocdev.jdocusaurus.annotations.enums.RelationType;

import java.util.List;

@JDocEntity(
        name = "Pedido",
        description = "Entidad que representa un pedido realizado por un cliente",
        table = "orders"
)
public class OrderEntity {

    @JDocField(description = "Identificador unico del pedido", nullable = false, constraints = "PK, AUTO_INCREMENT")
    private Long id;

    @JDocField(description = "Numero de referencia del pedido", example = "ORD-2026-001", nullable = false, constraints = "UNIQUE")
    private String orderNumber;

    @JDocField(description = "Importe total del pedido", example = "99.99", nullable = false)
    private Double totalAmount;

    @JDocField(description = "Estado del pedido: PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED", example = "PENDING", nullable = false)
    private String status;

    @JDocRelation(target = "Cliente", type = RelationType.MANY_TO_ONE, description = "Cliente que realizo el pedido")
    private Object customer;

    @JDocRelation(target = "LineaPedido", type = RelationType.ONE_TO_MANY, description = "Lineas del pedido")
    private List<Object> items;
}
