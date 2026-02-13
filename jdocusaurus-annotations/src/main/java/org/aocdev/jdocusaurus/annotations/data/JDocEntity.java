package org.aocdev.jdocusaurus.annotations.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documents a data entity for the Mermaid ER diagram and entity pages.
 *
 * <p>Generates a dedicated Markdown page under {@code data-model/} with
 * field tables, constraints, and relationships. Entities also appear in
 * the global ER diagram on the data model index page.
 *
 * <p>If the class has JPA annotations ({@code @Entity}, {@code @Column},
 * {@code @Id}, etc.), JDocusaurus reads them as fallback metadata without
 * requiring JPA as a compile dependency.
 *
 * <p><b>Example:</b>
 * <pre>{@code
 * @JDocEntity(name = "User", description = "Registered user", table = "users")
 * public class UserEntity { }
 * }</pre>
 *
 * @see JDocField
 * @see JDocRelation
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface JDocEntity {
    /** Display name. Defaults to the simple class name. */
    String name() default "";
    /** Description of the entity purpose. */
    String description() default "";
    /** Database table name. Can be auto-detected from JPA {@code @Table}. */
    String table() default "";
}
