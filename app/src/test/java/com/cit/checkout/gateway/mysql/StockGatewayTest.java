package com.cit.checkout.gateway.mysql;

import com.cit.checkout.domain.Product;
import com.cit.checkout.domain.Stock;
import com.cit.checkout.domain.exception.EntityNotFoundException;
import com.cit.checkout.gateway.StockGateway;
import com.cit.checkout.gateway.conf.MySQLDBRepositoryTest;
import com.cit.checkout.gateway.mysql.assembler.ProductAssembler;
import com.cit.checkout.gateway.mysql.model.ProductModel;
import com.cit.checkout.gateway.mysql.model.StockModel;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static br.com.six2six.fixturefactory.Fixture.from;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class StockGatewayTest extends MySQLDBRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockGateway stockGateway;

    private Product productMotoX4;
    private Product productScreenProtectorMotoX4;

    @Before
    public void setUp() {
        ProductModel productModelMotoX4 = from(ProductModel.class).gimme("model product moto x 4");
        productModelMotoX4 = productRepository.save(productModelMotoX4);

        ProductModel productModelCellphoneCaseMotoX4 = from(ProductModel.class).gimme("model product cellphone case moto x 4");
        productModelCellphoneCaseMotoX4 = productRepository.save(productModelCellphoneCaseMotoX4);

        ProductModel productModelScreenProtectorMotoX4 = from(ProductModel.class).gimme("model product without stock");
        productModelScreenProtectorMotoX4 = productRepository.save(productModelScreenProtectorMotoX4);

        StockModel stockModelProductMotoX4 = from(StockModel.class).gimme("model product 1 with one in stock");
        stockModelProductMotoX4.setProduct(productModelMotoX4);
        stockRepository.save(stockModelProductMotoX4);

        StockModel stockModelProductCellphoneCaseMotoX4 = from(StockModel.class).gimme("model product 2 with one in stock");
        stockModelProductCellphoneCaseMotoX4.setProduct(productModelCellphoneCaseMotoX4);
        stockRepository.save(stockModelProductCellphoneCaseMotoX4);

        productMotoX4 = ProductAssembler.assemble(productModelMotoX4);
        productScreenProtectorMotoX4 = ProductAssembler.assemble(productModelScreenProtectorMotoX4);
    }

    @Test
    public void testFindStockFromProductDontExists() {
        // GIVEN
        Product productDontExists = from(Product.class).gimme("product don't exists");

        // WHEN
        Throwable thrown = catchThrowable(() -> { stockGateway.getStock(productDontExists); });

        // THEN
        assertThat(thrown)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(format("Stock Not Found to Product Code: %s", productDontExists.getCode()));
    }

    @Test
    public void testFindStockFromOneProduct() {
        // WHEN
        Stock stock = stockGateway.getStock(productMotoX4);

        // THEN
        assertThat(stock).isNotNull();
        assertThat(stock.getId()).isNotNull();
        assertThat(stock.getProduct()).isEqualTo(productMotoX4);
        assertThat(stock.getTotal()).isEqualTo(1);
    }

    @Test
    public void testFindWithoutStockFromOneProduct() {
        // WHEN
        Throwable thrown = catchThrowable(() -> { stockGateway.getStock(productScreenProtectorMotoX4); });

        // THEN
        assertThat(thrown)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(format("Stock Not Found to Product Code: %s", productScreenProtectorMotoX4.getCode()));
    }

    @Test
    public void testLowStockFromProduct() {
        // WHEN
        stockGateway.lowStock(productMotoX4, 1);

        // THEN
        Optional<StockModel> stockModelOpt = stockRepository.findStockModelByProductId(productMotoX4.getId());

        assertThat(stockModelOpt.isPresent()).isTrue();

        StockModel stockModel = stockModelOpt.get();

        assertThat(stockModel).isNotNull();
        assertThat(stockModel.getTotal()).isEqualTo(0);
    }
}
