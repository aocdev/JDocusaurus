package org.aocdev.jdocusaurus.annotations.enums;

/**
 * Cardinality of a relationship between entities, for use with
 * {@link org.aocdev.jdocusaurus.annotations.data.JDocRelation#type()}.
 *
 * <p>Maps to Mermaid ER diagram relationship notation.
 *
 * @since 1.0.0
 */
public enum RelationType {
    /** One-to-one. Mermaid: {@code ||--||} */
    ONE_TO_ONE,
    /** One-to-many. Mermaid: <code>||--o{</code> */
    ONE_TO_MANY,
    /** Many-to-one. Mermaid: <code>}o--||</code> */
    MANY_TO_ONE,
    /** Many-to-many. Mermaid: <code>}o--o{</code> */
    MANY_TO_MANY
}
