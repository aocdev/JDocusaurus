package org.aocdev.jdocusaurus.annotations.flow;

import org.aocdev.jdocusaurus.annotations.enums.ParticipantType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares a participant in Mermaid sequence diagrams.
 *
 * <p>Participants appear as columns in sequence diagrams. The {@link #type()}
 * attribute determines the visual shape (actor, service, database, etc.).
 *
 * <p><b>Example:</b>
 * <pre>{@code
 * @JDocParticipant(name = "UserDB", alias = "Database", type = ParticipantType.DATABASE)
 * public class UserRepository { }
 * }</pre>
 *
 * @see JDocFlow
 * @see ParticipantType
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface JDocParticipant {
    /** Display name in the diagram header. */
    String name();
    /** Short alias used in diagram messages. Defaults to {@link #name()}. */
    String alias() default "";
    /** Visual type that determines the participant shape. */
    ParticipantType type() default ParticipantType.SERVICE;
}
