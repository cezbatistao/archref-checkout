package com.cit.checkout.domain.exception;

import com.cit.checkout.domain.Product;
import lombok.Getter;

@Getter
public class StockWithoutProductException extends RuntimeException {

    private static final long serialVersionUID = -9175696078007658525L;

    private final transient Product product;
    private final Integer quantityInStock;

    public StockWithoutProductException(Product product, Integer quantityInStock, String message) {
        super(message);

        this.product = product;
        this.quantityInStock = quantityInStock;
    }
}
