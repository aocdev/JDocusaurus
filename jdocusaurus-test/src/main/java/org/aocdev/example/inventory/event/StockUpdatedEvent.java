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
