package org.aocdev.example.event;

import org.aocdev.jdocusaurus.annotations.event.JDocEvent;

@JDocEvent(
        name = "OrderPlacedEvent",
        description = "Evento emitido cuando se realiza un nuevo pedido",
        topic = "order.placed",
        schema = "{ \"orderId\": \"long\", \"userId\": \"long\", \"totalAmount\": \"double\" }"
)
public class OrderPlacedEvent {
    private Long orderId;
    private Long userId;
    private Double totalAmount;
}
