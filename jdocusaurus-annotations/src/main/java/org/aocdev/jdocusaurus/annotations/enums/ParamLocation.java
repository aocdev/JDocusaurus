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
