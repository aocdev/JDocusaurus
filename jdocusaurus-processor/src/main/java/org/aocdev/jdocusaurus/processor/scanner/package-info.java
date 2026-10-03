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
/**
 * Scanners that extract documentation metadata from source code.
 *
 * <ul>
 *   <li>{@link org.aocdev.jdocusaurus.processor.scanner.AnnotationScanner} -
 *       reads {@code @JDoc*} annotations via the APT API</li>
 *   <li>{@link org.aocdev.jdocusaurus.processor.scanner.JpaScanner} -
 *       enriches entities with JPA metadata via {@code AnnotationMirror}</li>
 *   <li>{@link org.aocdev.jdocusaurus.processor.scanner.JavaParserScanner} -
 *       static analysis for automatic call graph detection</li>
 * </ul>
 *
 * @since 1.0.0
 */
package org.aocdev.jdocusaurus.processor.scanner;
