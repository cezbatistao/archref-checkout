package com.cit.checkout.gateway.feign.json;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentResponse {

    private PaymentStatus status;
    private String details;
    private String transactionId;

}
