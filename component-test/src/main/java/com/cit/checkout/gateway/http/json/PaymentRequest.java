package com.cit.checkout.gateway.http.json;

import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentRequest implements Serializable {

    private static final long serialVersionUID = 1397270981538195336L;

    private String type;
    private String cardValue;

}
