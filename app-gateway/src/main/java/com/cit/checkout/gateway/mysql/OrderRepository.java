package com.cit.checkout.gateway.mysql;

import com.cit.checkout.gateway.mysql.model.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, Long> {

    List<OrderModel> findAllByUsername(String username);

    OrderModel findByOrderNumber(Long orderNumber);
}
