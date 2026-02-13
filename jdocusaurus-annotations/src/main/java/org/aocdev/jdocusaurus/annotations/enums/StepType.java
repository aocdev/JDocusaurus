package org.aocdev.jdocusaurus.annotations.enums;

/**
 * Type of step in a Mermaid sequence diagram, for use with
 * {@link org.aocdev.jdocusaurus.annotations.flow.JDocFlowStep#type()}.
 *
 * <p>Maps to Mermaid sequence diagram syntax elements.
 *
 * @since 1.0.0
 */
public enum StepType {
    /** Synchronous call ({@code ->>}). */
    SYNC,
    /** Asynchronous call (<code>-)&gt;</code>). */
    ASYNC,
    /** Return/response ({@code -->>}). */
    RETURN,
    /** Annotation note over a participant. */
    NOTE,
    /** Alternative block ({@code alt/else}). */
    ALT,
    /** Loop block ({@code loop}). */
    LOOP,
    /** Optional block ({@code opt}). */
    OPT,
    /** Break block ({@code break}). */
    BREAK
}
