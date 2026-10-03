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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares a domain event for the event map and event documentation pages.
 *
 * <p>Generates a dedicated page under {@code events/} and a node in the
 * Mermaid event flow graph showing producers and consumers.
 *
 * <p><b>Example:</b>
 * <pre>{@code
 * @JDocEvent(
 *     name = "UserCreatedEvent",
 *     description = "Emitted when a user registers",
 *     topic = "user.created",
 *     schema = "{ \"userId\": \"long\", \"email\": \"string\" }"
 * )
 * public class UserCreatedEvent { }
 * }</pre>
 *
 * @see JDocProduces
 * @see JDocConsumes
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface JDocEvent {
    /** Event name (used as display label and cross-reference key). */
    String name();
    /** Description of when and why this event is emitted. */
    String description() default "";
    /** Message broker topic or channel name. */
    String topic() default "";
    /** JSON schema or structure of the event payload. */
    String schema() default "";
}
