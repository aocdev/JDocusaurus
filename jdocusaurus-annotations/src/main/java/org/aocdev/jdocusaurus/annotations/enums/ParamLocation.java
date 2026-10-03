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
 * Location of an HTTP parameter in the request, for use with
 * {@link org.aocdev.jdocusaurus.annotations.api.JDocParam#location()}.
 *
 * @since 1.0.0
 */
public enum ParamLocation {
    /** URL path segment (e.g., {@code /users/{id}}). */
    PATH,
    /** URL query string (e.g., {@code ?page=1}). */
    QUERY,
    /** Request body (typically JSON). */
    BODY,
    /** HTTP header value. */
    HEADER,
    /** HTTP cookie value. */
    COOKIE
}
