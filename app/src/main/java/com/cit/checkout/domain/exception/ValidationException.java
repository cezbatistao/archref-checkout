package com.cit.checkout.domain.exception;

import com.cit.checkout.domain.error.ConstraintError;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 6858187853503227198L;

    @Getter
    private final transient List<ConstraintError> constraintsErrors = new ArrayList<>();

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, List<ConstraintError> constraintsErrors) {
        super(message);
        this.constraintsErrors.addAll(constraintsErrors);
    }
}
