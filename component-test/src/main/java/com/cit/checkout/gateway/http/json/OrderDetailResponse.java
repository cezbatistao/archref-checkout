package com.cit.checkout.gateway.http.json;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class OrderDetailResponse implements Serializable {

    private static final long serialVersionUID = -675829385779473200L;

    private String productCode;
    private Integer quantity;
    private BigDecimal value;

}
