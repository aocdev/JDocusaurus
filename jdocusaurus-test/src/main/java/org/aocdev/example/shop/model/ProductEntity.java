package org.aocdev.example.shop.model;

import org.aocdev.jdocusaurus.annotations.data.JDocEntity;
import org.aocdev.jdocusaurus.annotations.data.JDocField;
import org.aocdev.jdocusaurus.annotations.data.JDocRelation;
import org.aocdev.jdocusaurus.annotations.enums.RelationType;

@JDocEntity(
        name = "Producto",
        description = "Entidad que representa un producto del catalogo",
        table = "products"
)
public class ProductEntity {

    @JDocField(description = "Identificador unico del producto", nullable = false, constraints = "PK, AUTO_INCREMENT")
    private Long id;

    @JDocField(description = "Nombre del producto", example = "Laptop Pro 15", nullable = false)
    private String name;

    @JDocField(description = "Descripcion detallada del producto", example = "Portatil de alto rendimiento con pantalla 15 pulgadas")
    private String description;

    @JDocField(description = "Precio unitario en EUR", example = "999.99", nullable = false)
    private Double price;

    @JDocField(description = "Codigo SKU unico del producto", example = "LAP-PRO-15-001", nullable = false, constraints = "UNIQUE")
    private String sku;

    @JDocRelation(target = "Categoria", type = RelationType.MANY_TO_ONE, description = "Categoria a la que pertenece el producto")
    private Object category;
}
