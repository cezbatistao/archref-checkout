package com.cit.checkout.gateway.http.json;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class OrderResponse implements Serializable {

    private static final long serialVersionUID = -4104685421873319771L;

    private String orderId;
    private PaymentResponse payment;

}
