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
package org.aocdev.example.inventory.model;

import org.aocdev.jdocusaurus.annotations.data.JDocEntity;
import org.aocdev.jdocusaurus.annotations.data.JDocField;
import org.aocdev.jdocusaurus.annotations.data.JDocRelation;
import org.aocdev.jdocusaurus.annotations.enums.RelationType;

@JDocEntity(
        name = "StockProducto",
        description = "Entidad que representa el stock disponible de un producto en un almacen",
        table = "product_stock"
)
public class ProductStockEntity {

    @JDocField(description = "Identificador unico del registro de stock", nullable = false, constraints = "PK, AUTO_INCREMENT")
    private Long id;

    @JDocField(description = "Codigo SKU del producto", example = "LAP-PRO-15-001", nullable = false, constraints = "INDEX")
    private String sku;

    @JDocField(description = "Cantidad disponible en stock", example = "150", nullable = false)
    private Integer quantity;

    @JDocField(description = "Stock minimo antes de generar alerta", example = "10", nullable = false)
    private Integer minStock;

    @JDocRelation(target = "Almacen", type = RelationType.MANY_TO_ONE, description = "Almacen donde se encuentra el stock")
    private Object warehouse;
}
