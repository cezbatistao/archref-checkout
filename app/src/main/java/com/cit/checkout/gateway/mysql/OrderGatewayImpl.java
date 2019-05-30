package com.cit.checkout.gateway.mysql;

import com.cit.checkout.domain.Order;
import com.cit.checkout.domain.exception.EntityNotFoundException;
import com.cit.checkout.gateway.OrderGateway;
import com.cit.checkout.gateway.mysql.assembler.OrderAssembler;
import com.cit.checkout.gateway.mysql.model.OrderModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OrderGatewayImpl implements OrderGateway {

    private OrderRepository orderRepository;

    @Autowired
    public OrderGatewayImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order register(Order order) {
        OrderModel orderModel = orderRepository.save(OrderAssembler.assemble(order));
        return converterToOrder(orderModel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAllByUsername(String username) {
        return orderRepository.findAllByUsername(username).stream()
                .map(this::converterToOrder)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Order findById(Long id) {
        Optional<OrderModel> order = orderRepository.findById(id);
        return converterToOrder(order.orElseThrow(() ->
                EntityNotFoundException.createOrderNotFoundException("Order Not Found")));
    }

    private Order converterToOrder(OrderModel orderModel) {
        return OrderAssembler.assemble(orderModel);
    }
}
