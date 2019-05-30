package com.cit.checkout.gateway;

import com.cit.checkout.domain.Product;

public interface ProductGateway {

    Product findByCode(String code);

}
