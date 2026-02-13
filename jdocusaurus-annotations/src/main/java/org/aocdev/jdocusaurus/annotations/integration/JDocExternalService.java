package org.aocdev.jdocusaurus.annotations.integration;

import org.aocdev.jdocusaurus.annotations.enums.ServiceType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documents an external service dependency.
 *
 * <p>Generates an entry in the Mermaid dependency map and the
 * integrations table under {@code integrations/index.md}.
 *
 * <p><b>Example:</b>
 * <pre>{@code
 * @JDocExternalService(
 *     name = "Payment Gateway",
 *     description = "Stripe payment processing",
 *     type = ServiceType.REST,
 *     url = "https://api.stripe.com/v1",
 *     owner = "Team Payments"
 * )
 * public class PaymentGatewayClient { }
 * }</pre>
 *
 * @see ServiceType
 * @since 1.0.0
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
public @interface JDocExternalService {
    /** Display name of the external service. */
    String name();
    /** Description of the integration purpose. */
    String description() default "";
    /** Base URL or connection string. */
    String url() default "";
    /** Communication protocol. */
    ServiceType type() default ServiceType.REST;
    /** Team or person responsible for the external service. */
    String owner() default "";
}
