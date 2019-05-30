package com.cit.checkout.gateway.http.assembler;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.cit.checkout.domain.PurchaseItem;
import com.cit.checkout.domain.ShoppingCart;
import com.cit.checkout.gateway.http.json.ItemRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ShoppingCartAssemblerTest {

    @Before
    public void setup() {
        FixtureFactoryLoader.loadTemplates("com.cit.checkout.templates");
    }

    @Test
    public void executeValidAssemblingToShoppingCard() {
        // GIVEN
        List<ItemRequest> itensRequest = Fixture.from(ItemRequest.class)
                .gimme(2, "1 product for 200", "3 product for 27.90");

        // WHEN
        ShoppingCart shoppingCart = ShoppingCartAssembler.assemble("user", itensRequest);

        // THEN
        assertThat(shoppingCart).isNotNull();

        assertThat(shoppingCart.getUsername()).isEqualTo("user");
        assertThat(shoppingCart.getItems()).hasSize(2);

        PurchaseItem purchaseItemProduct1Actual = shoppingCart.getItems()
                .stream()
                .filter(purchaseItem -> purchaseItem.getProductCode().equals("SKU-123-432"))
                .findFirst()
                .get();

        assertThat(purchaseItemProduct1Actual.getQuantity()).isEqualTo(1);

        PurchaseItem purchaseItemProduct2Actual = shoppingCart.getItems()
                .stream()
                .filter(purchaseItem -> purchaseItem.getProductCode().equals("SKU-322-222"))
                .findFirst()
                .get();

        assertThat(purchaseItemProduct2Actual.getQuantity()).isEqualTo(3);
    }
}
