package com.cit.checkout.usecase;

import com.cit.checkout.domain.ShoppingCart;
import com.cit.checkout.gateway.validator.DomainValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
@Component
public class ShoppingCartValidator {

    private DomainValidator<ShoppingCart> domainValidator;

    @Autowired
    public ShoppingCartValidator(DomainValidator<ShoppingCart> domainValidator) {
        this.domainValidator = domainValidator;
    }

    public void execute(ShoppingCart shoppingCart) {
        checkArgument(shoppingCart != null, "Shopping cart cannot be null");

        domainValidator.validate(shoppingCart);
    }
}
