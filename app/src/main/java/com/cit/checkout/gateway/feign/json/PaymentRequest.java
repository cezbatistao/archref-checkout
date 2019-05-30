package com.cit.checkout.gateway.feign.json;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class PaymentRequest {

    private PaymentType paymentType;
    private BigDecimal totalValue;
    private Integer installments;
    private String cardNumber;

}
