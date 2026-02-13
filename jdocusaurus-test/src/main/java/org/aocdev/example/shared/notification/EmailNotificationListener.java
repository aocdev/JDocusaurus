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
