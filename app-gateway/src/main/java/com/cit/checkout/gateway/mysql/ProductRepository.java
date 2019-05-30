package com.cit.checkout.gateway.mysql;

import com.cit.checkout.gateway.mysql.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {

    Optional<ProductModel> findByCode(String code);
}
