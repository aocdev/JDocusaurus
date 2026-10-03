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
