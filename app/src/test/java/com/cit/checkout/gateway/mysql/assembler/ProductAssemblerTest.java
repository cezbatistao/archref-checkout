package com.cit.checkout.gateway.mysql.assembler;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.cit.checkout.domain.Product;
import com.cit.checkout.gateway.mysql.model.ProductModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ProductAssemblerTest {

    @InjectMocks
    private ProductAssembler productAssembler;

    @Before
    public void setup() {
        FixtureFactoryLoader.loadTemplates("com.cit.checkout.templates");
    }

    @Test
    public void executeValidAssemblingProduct() {
        //Given
        ProductModel product = Fixture.from(ProductModel.class).gimme("model product moto x 4");

        //When
        Product assembled = productAssembler.assemble(product);

        //Then
        assertThat(assembled).isNotNull();
        assertThat(assembled.getId()).isEqualTo(product.getId());
        assertThat(assembled.getCode()).isEqualTo(product.getCode());
        assertThat(assembled.getName()).isEqualTo(product.getName());
        assertThat(assembled.getValue()).isEqualTo(product.getValue());
    }
}
