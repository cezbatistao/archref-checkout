package com.cit.checkout.gateway.mysql.assembler;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.cit.checkout.domain.Purchase;
import com.cit.checkout.gateway.mysql.model.PurchaseModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PurchaseAssemblerTest {

    @InjectMocks
    private PurchaseAssembler purchaseAssembler;

    @Before
    public void setup() {
        FixtureFactoryLoader.loadTemplates("com.cit.checkout.templates");
    }

    @Test
    public void executeValidAssemblingFromPurchase(){
        //Given
        Purchase purchase = Fixture.from(Purchase.class).gimme("purchase product 1");

        //When
        PurchaseModel assembled = purchaseAssembler.assemble(purchase);

        //Then
        assertThat(assembled).isNotNull();
        assertThat(assembled.getQuantity()).isEqualTo(purchase.getQuantity());
        assertThat(assembled.getProduct()).isNotNull();
        assertThat(assembled.getProduct().getId()).isEqualTo(purchase.getProduct().getId());
        assertThat(assembled.getProduct().getName()).isEqualTo(purchase.getProduct().getName());
        assertThat(assembled.getProduct().getValue()).isEqualTo(purchase.getProduct().getValue());
    }

    @Test
    public void executeValidAssemblingFromPurchaseModel(){
        //Given
        PurchaseModel purchase = Fixture.from(PurchaseModel.class).gimme("purchase model product 1");

        //When
        Purchase assembled = purchaseAssembler.assemble(purchase);

        //Then
        assertThat(assembled).isNotNull();
        assertThat(assembled.getQuantity()).isEqualTo(purchase.getQuantity());
        assertThat(assembled.getProduct()).isNotNull();
        assertThat(assembled.getProduct().getId()).isEqualTo(purchase.getProduct().getId());
        assertThat(assembled.getProduct().getName()).isEqualTo(purchase.getProduct().getName());
        assertThat(assembled.getProduct().getValue()).isEqualTo(purchase.getProduct().getValue());
    }
}
