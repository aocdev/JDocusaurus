package org.aocdev.example.inventory;

import org.aocdev.example.inventory.service.InventoryService;
import org.aocdev.jdocusaurus.annotations.api.*;
import org.aocdev.jdocusaurus.annotations.enums.HttpMethod;
import org.aocdev.jdocusaurus.annotations.enums.ParamLocation;

import java.util.List;

@JDocClass(
        name = "Inventory Controller",
        description = "Controlador para la gestion del inventario y stock de productos",
        basePath = "/api/v1/inventory",
        version = "v1",
        group = "Inventario"
)
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @JDocEndpoint(
            method = HttpMethod.GET,
            path = "/stock/{sku}",
            description = "Obtiene el stock disponible de un producto por su SKU",
            summary = "Consultar stock"
    )
    @JDocResponse(code = 200, description = "Stock obtenido correctamente")
    @JDocResponse(code = 404, description = "Producto no encontrado")
    public Object getStock(
            @JDocParam(
                    name = "sku",
                    description = "Codigo SKU del producto",
                    location = ParamLocation.PATH,
                    example = "LAP-PRO-15-001"
            ) String sku
    ) {
        return inventoryService.getStock(sku);
    }

    @JDocEndpoint(
            method = HttpMethod.PUT,
            path = "/stock/{sku}",
            description = "Actualiza el stock de un producto",
            summary = "Actualizar stock",
            auth = "Bearer JWT"
    )
    @JDocResponse(code = 200, description = "Stock actualizado correctamente")
    @JDocResponse(code = 404, description = "Producto no encontrado")
    @JDocResponse(code = 400, description = "Cantidad invalida")
    public void updateStock(
            @JDocParam(name = "sku", description = "Codigo SKU del producto", location = ParamLocation.PATH, example = "LAP-PRO-15-001") String sku,
            @JDocParam(name = "quantity", description = "Nueva cantidad de stock", location = ParamLocation.BODY, example = "150") Integer quantity
    ) {
        inventoryService.updateStock(sku, quantity);
    }

    @JDocEndpoint(
            method = HttpMethod.GET,
            path = "/alerts/low-stock",
            description = "Obtiene la lista de productos con stock por debajo del umbral minimo",
            summary = "Alertas de stock bajo"
    )
    @JDocResponse(code = 200, description = "Lista de alertas obtenida correctamente")
    public List<Object> getLowStockAlerts() {
        return (List<Object>) inventoryService.findLowStock();
    }
}
