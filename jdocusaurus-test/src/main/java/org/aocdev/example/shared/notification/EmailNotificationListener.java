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
package org.aocdev.example.shared.notification;

import org.aocdev.jdocusaurus.annotations.event.JDocConsumes;
import org.aocdev.example.customer.event.CustomerCreatedEvent;
import org.aocdev.example.order.event.OrderPlacedEvent;

public class EmailNotificationListener {

    @JDocConsumes(
            event = CustomerCreatedEvent.class,
            topic = "customer.created",
            description = "Envia email de bienvenida al nuevo cliente",
            group = "email-notification-group"
    )
    public void onCustomerCreated(Object event) {
    }

    @JDocConsumes(
            event = OrderPlacedEvent.class,
            topic = "order.placed",
            description = "Envia confirmacion de pedido al cliente",
            group = "email-notification-group"
    )
    public void onOrderPlaced(Object event) {
    }
}
