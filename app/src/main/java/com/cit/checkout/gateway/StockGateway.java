package com.cit.checkout.gateway;

import com.cit.checkout.domain.Product;
import com.cit.checkout.domain.Stock;

public interface StockGateway {

    Stock getStock(Product product);

    boolean lowStock(Product product, int quantityToPurchase);
}
