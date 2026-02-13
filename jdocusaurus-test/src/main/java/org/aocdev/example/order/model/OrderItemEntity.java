package org.aocdev.example.order.model;

import org.aocdev.jdocusaurus.annotations.data.JDocEntity;
import org.aocdev.jdocusaurus.annotations.data.JDocField;
import org.aocdev.jdocusaurus.annotations.data.JDocRelation;
import org.aocdev.jdocusaurus.annotations.enums.RelationType;

@JDocEntity(
        name = "LineaPedido",
        description = "Entidad que representa una linea individual dentro de un pedido",
        table = "order_items"
)
public class OrderItemEntity {

    @JDocField(description = "Identificador unico de la linea", nullable = false, constraints = "PK, AUTO_INCREMENT")
    private Long id;

    @JDocField(description = "Cantidad de unidades del producto", example = "2", nullable = false)
    private Integer quantity;

    @JDocField(description = "Precio unitario en el momento de la compra", example = "49.99", nullable = false)
    private Double unitPrice;

    @JDocRelation(target = "Pedido", type = RelationType.MANY_TO_ONE, description = "Pedido al que pertenece esta linea")
    private Object order;

    @JDocRelation(target = "Producto", type = RelationType.MANY_TO_ONE, description = "Producto incluido en esta linea")
    private Object product;
}
