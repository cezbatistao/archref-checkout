package com.cit.checkout.gateway.http.json;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@ApiModel(parent = PaymentRequest.class)
public class PaymentCardRequest extends PaymentRequest {

    @NotBlank
    @ApiModelProperty(value = "The card number", example = "4111111111111111")
    private String cardValue;

    @Builder
    public PaymentCardRequest(String type, String cardValue) {
        super(type);

        this.cardValue = cardValue;
    }
}
