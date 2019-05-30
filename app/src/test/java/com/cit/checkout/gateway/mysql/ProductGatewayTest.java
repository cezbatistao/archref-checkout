package com.cit.checkout.gateway.mysql;

import com.cit.checkout.domain.Product;
import com.cit.checkout.domain.exception.EntityNotFoundException;
import com.cit.checkout.gateway.ProductGateway;
import com.cit.checkout.gateway.conf.MySQLDBRepositoryTest;
import com.cit.checkout.gateway.mysql.model.ProductModel;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static br.com.six2six.fixturefactory.Fixture.from;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class ProductGatewayTest extends MySQLDBRepositoryTest {

    @Autowired
    private ProductGateway productGateway;

    @Autowired
    private ProductRepository productRepository;

    private ProductModel productModelMotoX4;

    @Before
    public void setUp() {
        productModelMotoX4 = from(ProductModel.class).gimme("model product moto x 4");
        productModelMotoX4 = productRepository.save(productModelMotoX4);

        ProductModel productModelCellphoneCaseMotoX4 = from(ProductModel.class).gimme("model product cellphone case moto x 4");
        productModelCellphoneCaseMotoX4 = productRepository.save(productModelCellphoneCaseMotoX4);
    }

    @Test
    public void testFindProductByCode() {
        // GIVEN
        Product productExpected = from(Product.class).gimme("moto x 4");

        String productCode = productModelMotoX4.getCode();

        // WHEN
        Product productActual = productGateway.findByCode(productCode);

        // THEN
        assertThat(productActual)
                .isNotNull()
                .isEqualToIgnoringGivenFields(productExpected, "id");
    }

    @Test
    public void testNotFindProductByCode() {
        // GIVEN
        String productCodeDontExists = "SKU-789-987";

        // WHEN
        Throwable thrown = catchThrowable(() -> { productGateway.findByCode(productCodeDontExists); });

        // THEN
        assertThat(thrown)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Product Not Found");
    }
}
