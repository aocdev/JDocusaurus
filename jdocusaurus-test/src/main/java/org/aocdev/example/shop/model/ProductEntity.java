/*
 * Copyright 2026 aocdev (Albert Ortells)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
