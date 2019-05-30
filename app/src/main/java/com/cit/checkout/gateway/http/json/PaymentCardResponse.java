package com.cit.checkout.gateway.http.json;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@ToString
public class PaymentCardResponse extends PaymentResponse {

    @ApiModelProperty(value = "Transaction id payment")
    private String transactionId;

    @Builder
    public PaymentCardResponse(String type, String transactionId) {
        super(type);
        this.transactionId = transactionId;
    }
}
