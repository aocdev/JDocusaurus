package org.aocdev.jdocusaurus.annotations.data;

import org.aocdev.jdocusaurus.annotations.enums.RelationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documents a relationship between entities in the ER diagram.
 *
 * <p>Generates a relationship line in the Mermaid ER diagram with the
 * specified cardinality. JPA relationship annotations ({@code @OneToMany},
 * {@code @ManyToOne}, etc.) are used as fallback when not explicitly set.
 *
 * <p><b>Example:</b>
 * <pre>{@code
 * @JDocRelation(target = "Order", type = RelationType.ONE_TO_MANY,
 *               description = "User orders")
 * private List<Order> orders;
 * }</pre>
 *
 * @see JDocEntity
 * @see RelationType
 * @since 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface JDocRelation {
    /** Target entity name. Defaults to the field's generic type. */
    String target() default "";
    /** Relationship cardinality. */
    RelationType type();
    /** Description of the relationship semantics. */
    String description() default "";
}
