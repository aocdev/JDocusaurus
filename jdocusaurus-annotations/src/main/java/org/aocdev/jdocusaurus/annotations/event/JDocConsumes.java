package org.aocdev.jdocusaurus.annotations.event;

import java.lang.annotation.*;

/**
 * Marks a class or method as a consumer of a {@link JDocEvent}.
 *
 * <p>This annotation is {@link Repeatable}. Creates a "consume" arrow
 * in the Mermaid event flow graph from the event node to this class.
 *
 * <p><b>Note:</b> The {@link #event()} attribute is read at compile time via
 * the {@code MirroredTypeException} pattern.
 *
 * <p><b>Example:</b>
 * <pre>{@code
 * @JDocConsumes(event = UserCreatedEvent.class,
 *               description = "Sends welcome email",
 *               group = "email-group")
 * public class EmailNotificationListener { }
 * }</pre>
 *
 * @see JDocEvent
 * @see JDocConsumesAll
 * @since 1.0.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
@Repeatable(JDocConsumesAll.class)
public @interface JDocConsumes {
    /** The event class being consumed. Read via {@code MirroredTypeException}. */
    Class<?> event() default Void.class;
    /** Topic or channel to subscribe to. */
    String topic() default "";
    /** Description of the consumption behavior. */
    String description() default "";
    /** Consumer group ID for load balancing. */
    String group() default "";
}
