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
