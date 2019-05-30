package com.cit.checkout.usecase;

import com.cit.checkout.domain.Payment;
import com.cit.checkout.gateway.validator.DomainValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
@Component
public class PaymentValidator {

    private DomainValidator<Payment> domainValidator;

    @Autowired
    public PaymentValidator(DomainValidator<Payment> domainValidator) {
        this.domainValidator = domainValidator;
    }

    public void execute(Payment payment) {
        checkArgument(payment != null, "Payment cannot be null");

        domainValidator.validate(payment);
    }
}
