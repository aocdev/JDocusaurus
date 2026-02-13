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
