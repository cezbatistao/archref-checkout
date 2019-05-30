package com.cit.checkout.gateway.http.json.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse implements Serializable {

    public static final String ERR_VALIDATION = "error.validation";
    public static final String ERR_METHOD_NOT_SUPPORTED = "error.methodNotSupported";
    public static final String ERR_INTERNAL_SERVER_ERROR = "error.internalServerError";
    public static final String ERR_FEATURE = "error.featureDisabled";
    public static final String ERR_TIMEOUT = "error.timeout";
    public static final String ERR_BAD_REQUEST = "error.badRequest";
    public static final String ERR_UNPROCESSABLE_ENTITY = "error.unprocessableEntity";
    public static final String ERR_UNAUTHORIZED_CREDIT_CARD = "error.unauthorizedCreditCard";

    private static final long serialVersionUID = 4435959686991135330L;

    private final Integer status;
    private final String error;
    private final String description;

    private List<ErrorField> errorFields;

    public ErrorResponse(StatusError statusError) {
        this(statusError, null);
    }

    public ErrorResponse(StatusError statusError, String description) {
        this.status = statusError.getStatus();
        this.error = statusError.getMessage();
        this.description = description;
    }

    public ErrorResponse(StatusError statusError, String description, List<ErrorField> errorFields) {
        this.status = statusError.getStatus();
        this.error = statusError.getMessage();
        this.description = description;
        this.errorFields = isEmpty(errorFields) ? null : errorFields;
    }
}
