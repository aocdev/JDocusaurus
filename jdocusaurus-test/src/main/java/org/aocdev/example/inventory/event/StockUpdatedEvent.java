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
package org.aocdev.example.inventory.event;

import org.aocdev.jdocusaurus.annotations.event.JDocEvent;

@JDocEvent(
        name = "StockUpdatedEvent",
        description = "Evento emitido cuando el stock de un producto cambia",
        topic = "inventory.stock.updated",
        schema = "{ \"sku\": \"string\", \"previousQuantity\": \"int\", \"newQuantity\": \"int\", \"warehouseId\": \"long\" }"
)
public class StockUpdatedEvent {
    private String sku;
    private Integer previousQuantity;
    private Integer newQuantity;
    private Long warehouseId;
}
