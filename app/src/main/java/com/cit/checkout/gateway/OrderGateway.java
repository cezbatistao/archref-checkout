package com.cit.checkout.gateway;

import com.cit.checkout.domain.Order;

import java.util.List;
import java.util.UUID;

public interface OrderGateway {

    Order register(Order order);

    List<Order> findAllByUsername(String username);

    Order findById(Long id);

}
