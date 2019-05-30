package com.cit.checkout.gateway.mysql;

import com.cit.checkout.domain.Product;
import com.cit.checkout.domain.Stock;
import com.cit.checkout.domain.exception.EntityNotFoundException;
import com.cit.checkout.gateway.StockGateway;
import com.cit.checkout.gateway.mysql.assembler.StockAssembler;
import com.cit.checkout.gateway.mysql.model.ProductModel;
import com.cit.checkout.gateway.mysql.model.StockModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.lang.String.format;

@Component
public class StockGatewayImpl implements StockGateway {

    private StockRepository stockRepository;
    private ProductRepository productRepository;

    @Autowired
    public StockGatewayImpl(StockRepository stockRepository, ProductRepository productRepository) {
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Stock getStock(Product product) {
        Optional<StockModel> stockModelOpt = stockRepository.findStockModelByProductId(product.getId());
        Optional<ProductModel> productModelOpt = productRepository.findById(product.getId());

        return productModelOpt.flatMap(productModel-> stockModelOpt
                .map(stockModel -> StockAssembler.assemble(stockModel, productModel)))
                .orElseThrow(()-> EntityNotFoundException.createStockNotFoundException(
                        format("Stock Not Found to Product Code: %s", product.getCode())));
    }

    @Override
    public boolean lowStock(Product product, int quantityToPurchase) {
        Optional<StockModel> stockModelOpt = stockRepository.findStockModelByProductId(product.getId());

        stockModelOpt.ifPresent(stockModel -> {
            stockModel.setTotal(stockModel.getTotal() - 1); // TODO refactory deixar isso no UseCase

            stockRepository.save(stockModel);
        });

        return false; // TODO o que eh isso?
    }
}
