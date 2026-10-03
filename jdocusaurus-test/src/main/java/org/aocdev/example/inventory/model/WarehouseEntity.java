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
