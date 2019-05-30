package com.cit.checkout.gateway.http.json;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class OrderResponse implements Serializable {

    private static final long serialVersionUID = -4104685421873319771L;

    @ApiModelProperty(value = "Order items")
    private String orderId;

    @ApiModelProperty(value = "Purchase date and time")
    private LocalDateTime purchaseDateTime;

    private PaymentResponse payment;
}
