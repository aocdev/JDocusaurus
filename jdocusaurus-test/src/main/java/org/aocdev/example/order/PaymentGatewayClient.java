package org.aocdev.example.order;

import org.aocdev.jdocusaurus.annotations.config.JDocConfig;
import org.aocdev.jdocusaurus.annotations.enums.RuleSeverity;
import org.aocdev.jdocusaurus.annotations.enums.ServiceType;
import org.aocdev.jdocusaurus.annotations.integration.JDocExternalService;
import org.aocdev.jdocusaurus.annotations.rule.JDocBusinessRule;

@JDocExternalService(
        name = "Payment Gateway",
        description = "Servicio externo de pasarela de pagos (Stripe)",
        type = ServiceType.REST,
        url = "https://api.stripe.com/v1",
        owner = "Team Payments"
)
@JDocConfig(key = "payment.stripe.api-key", description = "API key de Stripe", required = true, secret = true)
@JDocConfig(key = "payment.stripe.webhook-secret", description = "Secret para validar webhooks de Stripe", required = true, secret = true)
@JDocConfig(key = "payment.retry-max", description = "Numero maximo de reintentos en caso de fallo", defaultValue = "3", example = "5")
@JDocConfig(key = "payment.timeout-ms", description = "Timeout de conexion en milisegundos", defaultValue = "5000", example = "10000")
public class PaymentGatewayClient {

    @JDocExternalService(
            name = "Fraud Detection API",
            description = "Servicio de deteccion de fraude en transacciones",
            type = ServiceType.REST,
            url = "https://fraud-api.internal.company.com/v2",
            owner = "Team Security"
    )
    private Object fraudDetectionClient;

    @JDocBusinessRule(id = "BR-005", rule = "Todo pago superior a 1000 USD requiere verificacion anti-fraude", severity = RuleSeverity.MANDATORY, relatedRules = {"BR-006"})
    public Object processPayment(Object payment) {
        return null;
    }

    @JDocBusinessRule(id = "BR-006", rule = "Los reembolsos solo se permiten dentro de los 30 dias posteriores a la compra", severity = RuleSeverity.MANDATORY)
    @JDocBusinessRule(id = "BR-007", rule = "Se recomienda notificar al usuario por email tras un reembolso", severity = RuleSeverity.INFO)
    public Object refund(String transactionId) {
        return null;
    }
}
