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
package org.aocdev.example.customer.event;

import org.aocdev.jdocusaurus.annotations.event.JDocEvent;

@JDocEvent(
        name = "CustomerCreatedEvent",
        description = "Evento emitido cuando se crea un nuevo cliente en el sistema",
        topic = "customer.created",
        schema = "{ \"customerId\": \"long\", \"email\": \"string\", \"timestamp\": \"ISO-8601\" }"
)
public class CustomerCreatedEvent {
    private Long customerId;
    private String email;
    private String timestamp;
}
