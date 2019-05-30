package com.cit.checkout.gateway.http.json.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusError {
    //https://cloud.google.com/apis/design/errors

    INVALID_ARGUMENT(400,"error.invalidArgument"),
    OUT_OF_RANGE(400,"error.outOfRange"),

    UNAUTHENTICATED(401,"error.unauthenticated"),

    PERMISSION_DENIED(403,"error.permissionDenied"),
    FEATURE_DISABLED(403,"error.featureDisabled"),

    NOT_FOUND(404,"error.notFound"),

    METHOD_NOT_ALLOWED(405,"error.methodNotAllowed"),

    ABORTED(409,"error.aborted"),
    ALREADY_EXISTS(409,"error.alreadyExists"),

    VALIDATION(422,"error.validation"),

    RESOURCE_EXHAUSTED(429,"error.resourceExhausted"),

    CANCELLED(499,"error.cancelled"),

    DATA_LOSS(500,"error.dataLoss"),
    UNKNOWN(500,"error.unknown"),
    INTERNAL_SERVER_ERROR(500,"error.internalServerError"),

    NOT_IMPLEMENTED(501,"error.notImplemented"),

    UNAVAILABLE(503,"error.unavailable"),

    DEADLINE_EXCEEDED(504,"error.deadlineExceeded");

    private Integer status;
    private String message;
}
