package org.aocdev.jdocusaurus.annotations.enums;

/**
 * Standard HTTP methods for use with
 * {@link org.aocdev.jdocusaurus.annotations.api.JDocEndpoint#method()}.
 *
 * @since 1.0.0
 */
public enum HttpMethod {
    /** HTTP GET - retrieve a resource. */
    GET,
    /** HTTP POST - create a resource. */
    POST,
    /** HTTP PUT - replace/update a resource entirely. */
    PUT,
    /** HTTP DELETE - remove a resource. */
    DELETE,
    /** HTTP PATCH - partially update a resource. */
    PATCH,
    /** HTTP HEAD - retrieve headers only. */
    HEAD,
    /** HTTP OPTIONS - describe available methods. */
    OPTIONS
}
