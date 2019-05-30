package com.cit.checkout.gateway.http;

import com.cit.checkout.conf.log.LogKey;
import com.cit.checkout.domain.error.ConstraintError;
import com.cit.checkout.domain.exception.*;
import com.cit.checkout.gateway.http.json.error.ErrorField;
import com.cit.checkout.gateway.http.json.error.ErrorResponse;
import com.cit.checkout.gateway.http.json.error.StatusError;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import static net.logstash.logback.argument.StructuredArguments.value;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> processValidationError(MethodArgumentNotValidException e) {
        log.warn("An validation error occured: {}", e.getMessage());

        final BindingResult result = e.getBindingResult();
        List<ErrorField> errorFields = ErrorField.create(result);

        return createResponseEntity(StatusError.INVALID_ARGUMENT, null, errorFields);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> processValidationError(ConstraintViolationException e) {
        log.warn("An validation error occured: {}", e.getMessage());

        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        List<ErrorField> errorFields = ErrorField.create(constraintViolations);

        return createResponseEntity(StatusError.INVALID_ARGUMENT, null, errorFields);
    }

    @ExceptionHandler(FeatureException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> processFeatureException(FeatureException e) {
        log.warn("Occurred error for feature: {}", value(LogKey.FEATURE_KEY.toString(), e.getFeatureFlag().getKey()));

        final String description = "Feature disabled: " + e.getFeatureFlag().getKey();
        return createResponseEntity(StatusError.FEATURE_DISABLED, description);
    }

    @ExceptionHandler(StockWithoutProductException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> processFeatureException(StockWithoutProductException e) {
        log.warn("An validation error occured: {}", e.getMessage());

        return createResponseEntity(StatusError.VALIDATION, e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> processValidationException(ValidationException e) {
        log.warn("Validation error occurred: {}", e.getMessage(), e);

        List<ConstraintError> constraintsErrors = e.getConstraintsErrors();
        List<ErrorField> errorFields = ErrorField.create(constraintsErrors);

        return createResponseEntity(StatusError.VALIDATION, e.getMessage(), errorFields);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> processEntityNotFoundException(EntityNotFoundException e) {
        log.warn("Entity not found occurred: {}", e.getMessage(), e);

        ErrorField fieldError = ErrorField.builder()
                .messageCode(format("%s.dontExists", e.getEntity()))
                .message(e.getMessage())
                .fields(newArrayList(e.getEntity()))
                .build();

        return createResponseEntity(StatusError.INVALID_ARGUMENT, e.getMessage(), newArrayList(fieldError));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> processIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Illegal argument occurred: {}", e.getMessage(), e);
        return createResponseEntity(StatusError.INVALID_ARGUMENT, e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> processMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        log.warn("Method not allowed error occurred: {}", e.getMessage(), e);
        return createResponseEntity(StatusError.METHOD_NOT_ALLOWED, e.getMessage());
    }

    @ExceptionHandler(HystrixRuntimeException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> processHystrixException(HystrixRuntimeException e) {
        log.warn("A Hystrix Runtime Exception occurred: {}", e.getMessage());
        return createResponseEntity(StatusError.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> processException(Exception e) {
        log.error("An unexpected error occured: {}", value(LogKey.CAUSE.toString(), e.getMessage()), e);
        return createResponseEntity(StatusError.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotPaidException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> unprocessableCheckout(NotPaidException e) {
        log.warn("A unprocessable checkout error: {}", e.getMessage());
        return createResponseEntity(StatusError.VALIDATION, "Error processing order checkout");
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> unprocessableCheckout(ServletRequestBindingException e) {
        log.warn("A unprocessable checkout error: {}", e.getMessage());
        return createResponseEntity(StatusError.INVALID_ARGUMENT, e.getMessage());
    }

    private ResponseEntity<ErrorResponse> createResponseEntity(StatusError statusError) {
        return createResponseEntity(statusError, null, null);
    }

    private ResponseEntity<ErrorResponse> createResponseEntity(StatusError statusError, String description) {
        return ResponseEntity.status(statusError.getStatus())
                .body(new ErrorResponse(statusError, description, null));
    }

    private ResponseEntity<ErrorResponse> createResponseEntity(StatusError statusError, String description,
                                                               List<ErrorField> errorFields) {
        return ResponseEntity.status(statusError.getStatus())
                .body(new ErrorResponse(statusError, description, errorFields));
    }
}
