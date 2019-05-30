package com.cit.checkout.gateway.mysql.assembler;

import com.cit.checkout.domain.Product;
import com.cit.checkout.gateway.mysql.model.ProductModel;

import java.math.RoundingMode;

public class ProductAssembler {

    public static Product assemble(ProductModel productModel) {
        return Product.builder()
                .id(productModel.getId())
                .code(productModel.getCode())
                .name(productModel.getName())
                .value(productModel.getValue().setScale(2, RoundingMode.CEILING))
                .build();
    }

    public static ProductModel assemble(Product product) {
        return ProductModel.builder()
                .id(product.getId())
                .name(product.getName())
                .value(product.getValue().setScale(2, RoundingMode.CEILING))
                .build();
    }
}
