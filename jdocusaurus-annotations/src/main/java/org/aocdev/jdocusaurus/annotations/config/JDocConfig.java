package org.aocdev.jdocusaurus.annotations.config;

import java.lang.annotation.*;

/**
 * Documents a configuration property.
 *
 * <p>This annotation is {@link Repeatable}. Generates a row in the
 * configuration table under {@code config/index.md}. Properties marked
 * as {@link #secret()} have their values masked with {@code ***}.
 *
 * <p><b>Example:</b>
 * <pre>{@code
 * @JDocConfig(key = "payment.stripe.api-key", description = "Stripe API key",
 *             required = true, secret = true)
 * @JDocConfig(key = "payment.timeout-ms", description = "Timeout in ms",
 *             defaultValue = "5000", example = "10000")
 * public class PaymentConfig { }
 * }</pre>
 *
 * @see JDocConfigs
 * @since 1.0.0
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
@Repeatable(JDocConfigs.class)
public @interface JDocConfig {
    /** Configuration key (e.g., {@code "app.feature.enabled"}). */
    String key();
    /** Description of the property purpose. */
    String description() default "";
    /** Default value when not explicitly configured. */
    String defaultValue() default "";
    /** Whether the property must be set for the application to start. */
    boolean required() default false;
    /** Whether the value is sensitive (API keys, passwords). Masked in output. */
    boolean secret() default false;
    /** Example value for documentation. */
    String example() default "";
}
