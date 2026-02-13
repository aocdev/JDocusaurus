package org.aocdev.jdocusaurus.annotations.enums;

/**
 * Communication protocol of an external service, for use with
 * {@link org.aocdev.jdocusaurus.annotations.integration.JDocExternalService#type()}.
 *
 * @since 1.0.0
 */
public enum ServiceType {
    /** RESTful HTTP API. */
    REST,
    /** gRPC remote procedure call. */
    GRPC,
    /** SOAP/XML web service. */
    SOAP,
    /** GraphQL query API. */
    GRAPHQL,
    /** WebSocket real-time connection. */
    WEBSOCKET
}
