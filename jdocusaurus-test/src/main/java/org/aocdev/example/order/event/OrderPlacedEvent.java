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
package org.aocdev.example.order.event;

import org.aocdev.jdocusaurus.annotations.event.JDocEvent;

@JDocEvent(
        name = "OrderPlacedEvent",
        description = "Evento emitido cuando se realiza un nuevo pedido",
        topic = "order.placed",
        schema = "{ \"orderId\": \"long\", \"customerId\": \"long\", \"totalAmount\": \"double\" }"
)
public class OrderPlacedEvent {
    private Long orderId;
    private Long customerId;
    private Double totalAmount;
}
