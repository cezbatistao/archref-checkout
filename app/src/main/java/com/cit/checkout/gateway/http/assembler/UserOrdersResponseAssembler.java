package com.cit.checkout.gateway.http.assembler;

import com.cit.checkout.domain.Order;
import com.cit.checkout.gateway.http.json.OrderDetailResponse;
import com.cit.checkout.gateway.http.json.ProductResponse;
import com.cit.checkout.gateway.http.json.UserOrdersResponse;

import java.util.List;
import java.util.stream.Collectors;

public class UserOrdersResponseAssembler {

    private UserOrdersResponseAssembler() {
    }

    public static List<UserOrdersResponse> assemble(List<Order> orders) {
        return orders.stream()
                .map(order -> {
                    List<OrderDetailResponse> collect = order.getItems().stream()
                            .map(purchase -> OrderDetailResponse.builder()
                                    .product(ProductResponse.builder()
                                            .code(purchase.getProduct().getCode())
                                            .name(purchase.getProduct().getName())
                                            .value(purchase.getProduct().getValue())
                                            .build())
                                    .quantity(purchase.getQuantity())
                                    .value(purchase.getProduct().getValue())
                                    .build())
                            .collect(Collectors.toList());
                    return UserOrdersResponse.builder()
//                      .id(order.getId()) // TODO ???
                            .orderNumber(order.getOrderNumber().toString())
                            .value(order.getValue())
                            .orderDetails(collect)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
