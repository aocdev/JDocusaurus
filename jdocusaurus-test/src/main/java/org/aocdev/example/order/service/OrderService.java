package org.aocdev.example.order.service;

import org.aocdev.example.order.OrderRepository;
import org.aocdev.example.order.PaymentGatewayClient;
import org.aocdev.jdocusaurus.annotations.config.JDocConfig;
import org.aocdev.jdocusaurus.annotations.enums.RuleSeverity;
import org.aocdev.jdocusaurus.annotations.rule.JDocBusinessRule;

@JDocConfig(key = "order.max-items", description = "Numero maximo de lineas por pedido", defaultValue = "50", example = "100")
@JDocConfig(key = "order.retention-days", description = "Dias de retencion de pedidos cancelados", defaultValue = "365", example = "730")
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentGatewayClient paymentGatewayClient;

    public OrderService(OrderRepository orderRepository, PaymentGatewayClient paymentGatewayClient) {
        this.orderRepository = orderRepository;
        this.paymentGatewayClient = paymentGatewayClient;
    }

    @JDocBusinessRule(id = "BR-008", rule = "Un pedido requiere un cliente valido y activo en el sistema", severity = RuleSeverity.MANDATORY, relatedRules = {"BR-001"})
    @JDocBusinessRule(id = "BR-009", rule = "El total del pedido debe ser igual a la suma de precio unitario x cantidad de cada linea", severity = RuleSeverity.MANDATORY)
    public Object create(Object order) {
        paymentGatewayClient.processPayment(order);
        return orderRepository.save(order);
    }

    public Object findById(Long id) {
        return orderRepository.findById(id);
    }

    public Object findByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public Object cancel(Long id) {
        Object order = orderRepository.findById(id);
        if (order != null) {
            return orderRepository.save(order);
        }
        return null;
    }
}
