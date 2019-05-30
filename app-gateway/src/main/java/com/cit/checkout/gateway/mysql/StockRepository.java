package com.cit.checkout.gateway.mysql;

import com.cit.checkout.gateway.mysql.model.StockModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<StockModel, Long> {

    Optional<StockModel> findStockModelByProductId(Long productId);
}
