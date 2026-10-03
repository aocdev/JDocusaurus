/*
 * Copyright 2026 aocdev (Albert Ortells)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.aocdev.jdocusaurus.annotations.event;

import java.lang.annotation.*;

/**
 * Marks a class or method as a producer of a {@link JDocEvent}.
 *
 * <p>This annotation is {@link Repeatable}. Creates a "produce" arrow
 * in the Mermaid event flow graph from this class to the event node.
 *
 * <p><b>Note:</b> The {@link #event()} attribute is read at compile time via
 * the {@code MirroredTypeException} pattern.
 *
 * <p><b>Example:</b>
 * <pre>{@code
 * @JDocProduces(event = UserCreatedEvent.class,
 *               topic = "user.created",
 *               description = "Emits on user creation")
 * public Object createUser(Object user) { }
 * }</pre>
 *
 * @see JDocEvent
 * @see JDocProducesAll
 * @since 1.0.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
@Repeatable(JDocProducesAll.class)
public @interface JDocProduces {
    /** The event class being produced. Read via {@code MirroredTypeException}. */
    Class<?> event() default Void.class;
    /** Topic or channel where the event is published. */
    String topic() default "";
    /** Description of the production context. */
    String description() default "";
    /** Whether the event is published asynchronously. */
    boolean async() default true;
}
