package org.aocdev.example;

import org.aocdev.jdocusaurus.annotations.event.JDocConsumes;
import org.aocdev.example.event.UserCreatedEvent;
import org.aocdev.example.event.OrderPlacedEvent;

public class EmailNotificationListener {

    @JDocConsumes(
            event = UserCreatedEvent.class,
            topic = "user.created",
            description = "Envia email de bienvenida al nuevo usuario",
            group = "email-notification-group"
    )
    public void onUserCreated(Object event) {
    }

    @JDocConsumes(
            event = OrderPlacedEvent.class,
            topic = "order.placed",
            description = "Envia confirmacion de pedido al usuario",
            group = "email-notification-group"
    )
    public void onOrderPlaced(Object event) {
    }
}
