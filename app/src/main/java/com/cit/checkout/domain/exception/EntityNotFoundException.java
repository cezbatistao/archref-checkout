package com.cit.checkout.domain.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {

    private String entity;

    public static EntityNotFoundException createProductNotFoundException(String message) {
        return new EntityNotFoundException("product", message);
    }

    public static EntityNotFoundException createOrderNotFoundException(String message) {
        return new EntityNotFoundException("order", message);
    }

    public static EntityNotFoundException createStockNotFoundException(String message) {
        return new EntityNotFoundException("stock", message);
    }

    private EntityNotFoundException(String entity, String message) {
        super(message);

        this.entity = entity;
    }
}
