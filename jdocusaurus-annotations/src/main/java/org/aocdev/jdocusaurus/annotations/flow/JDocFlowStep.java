package org.aocdev.jdocusaurus.annotations.flow;

import org.aocdev.jdocusaurus.annotations.enums.StepType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a single step within a {@link JDocFlow} sequence diagram.
 *
 * <p>This annotation is {@link Repeatable}, allowing multiple steps on the
 * same method. Steps are ordered by {@link #order()} and rendered as arrows
 * between participants in the Mermaid sequence diagram.
 *
 * <p><b>Example:</b>
 * <pre>{@code
 * @JDocFlowStep(flow = "user-registration", order = 1,
 *     from = "Client", to = "Controller", message = "POST /users")
 * @JDocFlowStep(flow = "user-registration", order = 2,
 *     from = "Controller", to = "Service", message = "create(user)")
 * public Object createUser(Object user) { }
 * }</pre>
 *
 * @see JDocFlow
 * @see JDocFlowSteps
 * @see StepType
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@Repeatable(JDocFlowSteps.class)
public @interface JDocFlowStep {
    /** Name of the parent {@link JDocFlow} this step belongs to. */
    String flow();
    /** Execution order within the flow (lower values first). */
    int order() default 0;
    /** Source participant name. */
    String from();
    /** Target participant name. */
    String to();
    /** Message label on the arrow (e.g., method call or HTTP request). */
    String message();
    /** Arrow type in the diagram. */
    StepType type() default StepType.SYNC;
    /** Return message label (dashed arrow back). Empty means no return arrow. */
    String returnMessage() default "";
    /** Condition label for {@link StepType#ALT} or {@link StepType#OPT} blocks. */
    String condition() default "";
    /** Note text to display over a participant. */
    String note() default "";
}
