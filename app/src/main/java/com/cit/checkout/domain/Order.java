package com.cit.checkout.domain;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class Order {

    private Long id;

    @NonNull
    private Long orderNumber;

    @NonNull
    private String username;

    @NonNull
    private List<Purchase> items;

    @NonNull
    private BigDecimal value;

    private OrderConfirmation orderConfirmation;

}
