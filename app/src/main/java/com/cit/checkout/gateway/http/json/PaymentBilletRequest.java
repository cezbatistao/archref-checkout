package com.cit.checkout.gateway.http.json;

import io.swagger.annotations.ApiModel;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@ToString
@ApiModel(parent = PaymentRequest.class)
public class PaymentBilletRequest extends PaymentRequest {

    @Builder
    public PaymentBilletRequest(String type) {
        super(type);
    }
}
