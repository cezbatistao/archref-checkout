package com.cit.checkout.gateway.http.json;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class ProductResponse implements Serializable {

    private String name;
    private String code;
    private BigDecimal value;

}
