package com.cit.checkout.gateway.http.json;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@ToString
public abstract class PaymentResponse {

    private String type;

}
