package org.aocdev.example;

import org.aocdev.jdocusaurus.annotations.event.JDocConsumes;
import org.aocdev.jdocusaurus.annotations.event.JDocProduces;
import org.aocdev.example.event.OrderPlacedEvent;

public class InventoryService {

    @JDocConsumes(
            event = OrderPlacedEvent.class,
            topic = "order.placed",
            description = "Actualiza el inventario al recibir un nuevo pedido",
            group = "inventory-group"
    )
    public void onOrderPlaced(Object event) {
    }

    @JDocProduces(
            topic = "order.placed",
            description = "Produce evento de pedido desde el servicio de inventario"
    )
    public void placeOrder(Object order) {
    }
}
