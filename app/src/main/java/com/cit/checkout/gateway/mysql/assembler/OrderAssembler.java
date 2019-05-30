package com.cit.checkout.gateway.mysql.assembler;

import com.cit.checkout.domain.Order;
import com.cit.checkout.gateway.mysql.model.OrderModel;

import java.math.RoundingMode;
import java.util.stream.Collectors;

public class OrderAssembler {

    public static Order assemble(OrderModel orderModel) {
        Order order = Order.builder()
                .id(orderModel.getId())
                .username(orderModel.getUsername())
                .orderNumber(orderModel.getOrderNumber())
                .items(orderModel.getItems().stream()
                        .map(PurchaseAssembler::assemble)
                        .collect(Collectors.toList()))
                .value(orderModel.getValue().setScale(2, RoundingMode.CEILING))
                .build();

        return order;
    }

    public static OrderModel assemble(Order order) {
        OrderModel orderModel = OrderModel.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .username(order.getUsername())
                .items(order.getItems().stream()
                        .map(PurchaseAssembler::assemble)
                        .collect(Collectors.toList()))
                .value(order.getValue().setScale(2, RoundingMode.CEILING))
                .build();

        orderModel.getItems().forEach(purchaseModel -> purchaseModel.setOrder(orderModel));

        return orderModel;
    }
}
