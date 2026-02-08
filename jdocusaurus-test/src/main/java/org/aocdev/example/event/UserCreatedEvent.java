package org.aocdev.example.event;

import org.aocdev.jdocusaurus.annotations.event.JDocEvent;

@JDocEvent(
        name = "UserCreatedEvent",
        description = "Evento emitido cuando se crea un nuevo usuario en el sistema",
        topic = "user.created",
        schema = "{ \"userId\": \"long\", \"email\": \"string\", \"timestamp\": \"ISO-8601\" }"
)
public class UserCreatedEvent {
    private Long userId;
    private String email;
    private String timestamp;
}
