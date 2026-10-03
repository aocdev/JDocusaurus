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
