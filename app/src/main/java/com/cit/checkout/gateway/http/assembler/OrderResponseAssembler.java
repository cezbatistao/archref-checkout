package com.cit.checkout.gateway.http.assembler;

import com.cit.checkout.domain.Order;
import com.cit.checkout.gateway.http.json.OrderResponse;

public class OrderResponseAssembler {

    private OrderResponseAssembler() {
    }

    public static OrderResponse assemble(Order order) {
        return OrderResponse.builder()
                .orderId(order.getOrderNumber().toString())
                .payment(PaymentResponseAssembler.assemble(order.getOrderConfirmation()))
                .build();
    }
}
