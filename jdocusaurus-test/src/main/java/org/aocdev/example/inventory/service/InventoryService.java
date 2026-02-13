package org.aocdev.example.inventory.service;

import org.aocdev.example.inventory.event.StockUpdatedEvent;
import org.aocdev.example.order.event.OrderPlacedEvent;
import org.aocdev.jdocusaurus.annotations.config.JDocConfig;
import org.aocdev.jdocusaurus.annotations.enums.RuleSeverity;
import org.aocdev.jdocusaurus.annotations.event.JDocConsumes;
import org.aocdev.jdocusaurus.annotations.event.JDocProduces;
import org.aocdev.jdocusaurus.annotations.rule.JDocBusinessRule;

@JDocConfig(key = "inventory.low-stock-threshold", description = "Umbral minimo de stock para generar alertas", defaultValue = "10", example = "5")
public class InventoryService {

    @JDocConsumes(
            event = OrderPlacedEvent.class,
            topic = "order.placed",
            description = "Actualiza el inventario al recibir un nuevo pedido",
            group = "inventory-group"
    )
    @JDocBusinessRule(id = "BR-010", rule = "No se permite vender mas unidades de las disponibles en stock (prevenir overselling)", severity = RuleSeverity.MANDATORY, relatedRules = {"BR-009"})
    public void onOrderPlaced(Object event) {
    }

    @JDocProduces(
            event = StockUpdatedEvent.class,
            topic = "inventory.stock.updated",
            description = "Produce evento cuando el stock de un producto cambia"
    )
    public void updateStock(String sku, Integer quantity) {
    }

    public Object getStock(String sku) {
        return null;
    }

    public Object findLowStock() {
        return null;
    }
}
