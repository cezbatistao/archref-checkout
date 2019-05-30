package com.cit.checkout.gateway.mysql;

import com.cit.checkout.domain.Product;
import com.cit.checkout.domain.exception.EntityNotFoundException;
import com.cit.checkout.gateway.ProductGateway;
import com.cit.checkout.gateway.mysql.assembler.ProductAssembler;
import com.cit.checkout.gateway.mysql.model.ProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductGatewayImpl implements ProductGateway {

    private ProductRepository productRepository;

    @Autowired
    public ProductGatewayImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product findByCode(String code) {
        Optional<ProductModel> productModel = productRepository.findByCode(code);
        return ProductAssembler.assemble(productModel.orElseThrow(() ->
                EntityNotFoundException.createProductNotFoundException("Product Not Found")));
    }
}
