package com.cit.checkout.gateway.validator;

import com.cit.checkout.domain.error.ConstraintError;
import com.cit.checkout.domain.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DomainValidator<T> {

    private Validator validator;

    @Autowired
    public DomainValidator(Validator validator) {
        this.validator = validator;
    }

    public void validate(T domain) {
        log.info("Checking validation error to: {}", domain);

        Set<ConstraintViolation<T>> validationErrors = validator.validate(domain);

        if (!validationErrors.isEmpty()) {
            List<ConstraintError> errors = create(validationErrors);

            log.info("Validation error. Reason: {}", errors);

            throw new ValidationException("error.validationFields", errors);
        }
    }

    private List<ConstraintError> create(final Set<ConstraintViolation<T>> validationErrors) {
        return validationErrors.stream()
                .map(constraintViolation ->
                    ConstraintError.builder()
                            .message(constraintViolation.getMessage())
                            .messageTemplate(constraintViolation.getMessageTemplate())
                            .propertyPath(constraintViolation.getPropertyPath().toString())
                            .invalidValue(constraintViolation.getInvalidValue())
                            .build())
                .collect(Collectors.toList());
    }
}
