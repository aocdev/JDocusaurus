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
 * Severity level of a business rule, for use with
 * {@link org.aocdev.jdocusaurus.annotations.rule.JDocBusinessRule#severity()}.
 *
 * <p>Rules are grouped by severity in the generated documentation.
 *
 * @since 1.0.0
 */
public enum RuleSeverity {
    /** Must be enforced; violation causes a hard error. */
    MANDATORY,
    /** Should be followed; violation triggers a warning. */
    WARNING,
    /** Informational guideline. */
    INFO
}
