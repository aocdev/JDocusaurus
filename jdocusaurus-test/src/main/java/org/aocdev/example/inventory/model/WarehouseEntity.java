package org.aocdev.example.inventory.model;

import org.aocdev.jdocusaurus.annotations.data.JDocEntity;
import org.aocdev.jdocusaurus.annotations.data.JDocField;
import org.aocdev.jdocusaurus.annotations.data.JDocRelation;
import org.aocdev.jdocusaurus.annotations.enums.RelationType;

import java.util.List;

@JDocEntity(
        name = "Almacen",
        description = "Entidad que representa un almacen o centro de distribucion",
        table = "warehouses"
)
public class WarehouseEntity {

    @JDocField(description = "Identificador unico del almacen", nullable = false, constraints = "PK, AUTO_INCREMENT")
    private Long id;

    @JDocField(description = "Nombre del almacen", example = "Almacen Central Madrid", nullable = false)
    private String name;

    @JDocField(description = "Ubicacion del almacen", example = "Madrid, Espana", nullable = false)
    private String location;

    @JDocRelation(target = "StockProducto", type = RelationType.ONE_TO_MANY, description = "Stock de productos en este almacen")
    private List<Object> productStock;
}
