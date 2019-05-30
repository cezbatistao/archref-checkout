package com.cit.checkout.usecase;

import com.cit.checkout.domain.Order;
import com.cit.checkout.gateway.OrderGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SearchUserOrders {

    private OrderGateway orderGateway;

    @Autowired
    public SearchUserOrders(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    public List<Order> execute(String username) {
        return orderGateway.findAllByUsername(username);
    }
}
