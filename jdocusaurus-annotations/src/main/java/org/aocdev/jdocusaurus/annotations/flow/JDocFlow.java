package org.aocdev.jdocusaurus.annotations.flow;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares a business flow that produces a Mermaid sequence diagram.
 *
 * <p>Can be placed on a class (to scope all its steps) or on a method
 * (to declare a single-method flow). Steps within the flow are defined
 * with {@link JDocFlowStep} or detected automatically via JavaParser
 * static analysis.
 *
 * <p><b>Example:</b>
 * <pre>{@code
 * @JDocFlow(
 *     name = "user-registration",
 *     title = "User Registration",
 *     description = "Complete registration flow"
 * )
 * public class UserController { }
 * }</pre>
 *
 * @see JDocFlowStep
 * @see JDocParticipant
 * @since 1.0.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface JDocFlow {
    /** Unique identifier for the flow (used as filename and cross-reference key). */
    String name();
    /** Description of the flow purpose. */
    String description();
    /** Display title in generated docs. Defaults to {@link #name()} if empty. */
    String title() default "";
}
