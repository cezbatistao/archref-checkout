package com.cit.checkout.usecase;

import com.cit.checkout.domain.Order;
import com.cit.checkout.gateway.OrderGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DetailOrder {

    private OrderGateway orderGateway;

    @Autowired
    public DetailOrder(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    public Order execute(Long id) {
        return orderGateway.findById(id);
    }
}
