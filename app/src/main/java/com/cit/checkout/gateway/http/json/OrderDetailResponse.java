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
public class OrderDetailResponse implements Serializable {

    private ProductResponse product;
    private Integer quantity;
    private BigDecimal value;

}
