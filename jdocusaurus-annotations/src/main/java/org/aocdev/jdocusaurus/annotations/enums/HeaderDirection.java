package org.aocdev.jdocusaurus.annotations.enums;

/**
 * Direction of an HTTP header, for use with
 * {@link org.aocdev.jdocusaurus.annotations.api.JDocHeader#direction()}.
 *
 * @since 1.0.0
 */
public enum HeaderDirection {
    /** Header sent in the client request. */
    REQUEST,
    /** Header returned in the server response. */
    RESPONSE,
    /** Header used in both request and response. */
    BOTH
}
