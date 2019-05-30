package com.cit.checkout.gateway.http.json;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class UserOrdersResponse implements Serializable {

    private String orderNumber;
    private BigDecimal value;
    private List<OrderDetailResponse> orderDetails;

}
