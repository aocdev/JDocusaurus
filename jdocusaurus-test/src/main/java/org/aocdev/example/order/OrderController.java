package org.aocdev.example.order;

import org.aocdev.example.order.event.OrderPlacedEvent;
import org.aocdev.example.order.service.OrderService;
import org.aocdev.jdocusaurus.annotations.api.*;
import org.aocdev.jdocusaurus.annotations.enums.HttpMethod;
import org.aocdev.jdocusaurus.annotations.enums.ParamLocation;
import org.aocdev.jdocusaurus.annotations.enums.ParticipantType;
import org.aocdev.jdocusaurus.annotations.event.JDocProduces;
import org.aocdev.jdocusaurus.annotations.flow.*;

import java.util.List;

@JDocClass(
        name = "Order Controller",
        description = "Controlador para la gestion de pedidos de la tienda",
        basePath = "/api/v1/orders",
        version = "v1",
        group = "Pedidos"
)
@JDocParticipant(name = "OrderController", alias = "Controller", type = ParticipantType.SERVICE)
@JDocFlow(name = "order-placement", description = "Flujo completo de creacion de un nuevo pedido", title = "Creacion de Pedido")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @JDocEndpoint(
            method = HttpMethod.POST,
            path = "/",
            description = "Crea un nuevo pedido en el sistema",
            summary = "Crear pedido",
            auth = "Bearer JWT"
    )
    @JDocResponse(code = 201, description = "Pedido creado exitosamente")
    @JDocResponse(code = 400, description = "Datos de entrada invalidos")
    @JDocResponse(code = 422, description = "Stock insuficiente para uno o mas productos")
    @JDocFlowStep(flow = "order-placement", order = 1, from = "Client", to = "OrderController", message = "POST /api/v1/orders")
    @JDocFlowStep(flow = "order-placement", order = 2, from = "OrderController", to = "OrderService", message = "create(order)")
    @JDocFlowStep(flow = "order-placement", order = 3, from = "OrderService", to = "PaymentGateway", message = "processPayment(payment)")
    @JDocFlowStep(flow = "order-placement", order = 4, from = "OrderService", to = "OrderRepository", message = "save(order)", returnMessage = "savedOrder")
    @JDocFlowStep(flow = "order-placement", order = 5, from = "OrderController", to = "Client", message = "201 Created")
    @JDocProduces(event = OrderPlacedEvent.class, topic = "order.placed", description = "Emite evento al crear pedido")
    public Object createOrder(
            @JDocParam(
                    name = "order",
                    description = "Datos del nuevo pedido",
                    location = ParamLocation.BODY
            ) Object order
    ) {
        return orderService.create(order);
    }

    @JDocEndpoint(
            method = HttpMethod.GET,
            path = "/{id}",
            description = "Obtiene los datos de un pedido por su identificador",
            summary = "Obtener pedido"
    )
    @JDocResponse(code = 200, description = "Pedido encontrado")
    @JDocResponse(code = 404, description = "Pedido no encontrado")
    public Object getOrderById(
            @JDocParam(
                    name = "id",
                    description = "Identificador unico del pedido",
                    location = ParamLocation.PATH,
                    example = "456"
            ) Long id
    ) {
        return orderService.findById(id);
    }

    @JDocEndpoint(
            method = HttpMethod.GET,
            path = "/customer/{customerId}",
            description = "Obtiene los pedidos de un cliente",
            summary = "Pedidos por cliente"
    )
    @JDocResponse(code = 200, description = "Lista de pedidos obtenida correctamente")
    @JDocResponse(code = 404, description = "Cliente no encontrado")
    public List<Object> getOrdersByCustomer(
            @JDocParam(
                    name = "customerId",
                    description = "Identificador del cliente",
                    location = ParamLocation.PATH,
                    example = "123"
            ) Long customerId
    ) {
        return (List<Object>) orderService.findByCustomer(customerId);
    }

    @JDocEndpoint(
            method = HttpMethod.PUT,
            path = "/{id}/cancel",
            description = "Cancela un pedido existente",
            summary = "Cancelar pedido",
            auth = "Bearer JWT"
    )
    @JDocResponse(code = 200, description = "Pedido cancelado correctamente")
    @JDocResponse(code = 404, description = "Pedido no encontrado")
    @JDocResponse(code = 409, description = "El pedido no puede ser cancelado en su estado actual")
    public Object cancelOrder(
            @JDocParam(name = "id", description = "Identificador del pedido a cancelar", location = ParamLocation.PATH, example = "456") Long id
    ) {
        return orderService.cancel(id);
    }
}
