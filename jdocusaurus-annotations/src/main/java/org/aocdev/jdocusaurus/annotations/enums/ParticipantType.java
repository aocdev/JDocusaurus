package org.aocdev.jdocusaurus.annotations.enums;

/**
 * Type of participant in a Mermaid sequence diagram, for use with
 * {@link org.aocdev.jdocusaurus.annotations.flow.JDocParticipant#type()}.
 *
 * <p>Determines the visual representation in the generated diagram.
 *
 * @since 1.0.0
 */
public enum ParticipantType {
    /** Human user or external client. */
    ACTOR,
    /** Internal microservice or component. */
    SERVICE,
    /** Data store (SQL, NoSQL, etc.). */
    DATABASE,
    /** Message broker or queue (Kafka, RabbitMQ, etc.). */
    QUEUE,
    /** Cache system (Redis, Memcached, etc.). */
    CACHE,
    /** External third-party service. */
    EXTERNAL
}
