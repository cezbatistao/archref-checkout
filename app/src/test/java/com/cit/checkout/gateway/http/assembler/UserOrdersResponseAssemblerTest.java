package com.cit.checkout.gateway.http.assembler;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.cit.checkout.domain.Order;
import com.cit.checkout.gateway.http.json.OrderDetailResponse;
import com.cit.checkout.gateway.http.json.UserOrdersResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class UserOrdersResponseAssemblerTest {

    @Before
    public void setup() {
        FixtureFactoryLoader.loadTemplates("com.cit.checkout.templates");
    }

    @Test
    public void executeValidAssemblingToOrderResponse() {
        // GIVEN
        List<Order> orders = Fixture.from(Order.class).gimme(2, "order 1", "order 2");

        // THEN
        List<UserOrdersResponse> userOrdersResponse = UserOrdersResponseAssembler.assemble(orders);

        // WHEN
        assertThat(userOrdersResponse).hasSize(2);

        // AND
        UserOrdersResponse userOrder1ResponseActual = userOrdersResponse.stream()
                .filter(userOrderResponse -> userOrderResponse.getOrderNumber().equals("5435435"))
                .findFirst()
                .get();

        assertThat(userOrder1ResponseActual.getValue()).isEqualTo(new BigDecimal("999.99"));
        assertThat(userOrder1ResponseActual.getOrderDetails()).hasSize(1);

        OrderDetailResponse orderDetailResponseProduct1 = userOrder1ResponseActual.getOrderDetails().stream()
                .findFirst()
                .get();

        assertThat(orderDetailResponseProduct1.getProduct()).isNotNull();
        assertThat(orderDetailResponseProduct1.getProduct().getCode()).isEqualTo("SKU-123-123");
        assertThat(orderDetailResponseProduct1.getProduct().getName()).isEqualTo("Moto X 4º");
        assertThat(orderDetailResponseProduct1.getProduct().getValue()).isEqualTo(new BigDecimal("999.99"));
        assertThat(orderDetailResponseProduct1.getQuantity()).isEqualTo(1);

        // AND
        UserOrdersResponse userOrder2ResponseActual = userOrdersResponse.stream()
                .filter(userOrderResponse -> userOrderResponse.getOrderNumber().equals("23423532"))
                .findFirst()
                .get();

        assertThat(userOrder2ResponseActual.getValue()).isEqualTo(new BigDecimal("21.80"));
        assertThat(userOrder2ResponseActual.getOrderDetails()).hasSize(1);

        OrderDetailResponse orderDetailResponseProduct2 = userOrder2ResponseActual.getOrderDetails().stream()
                .findFirst()
                .get();

        assertThat(orderDetailResponseProduct2.getProduct()).isNotNull();
        assertThat(orderDetailResponseProduct2.getProduct().getCode()).isEqualTo("SKU-321-321");
        assertThat(orderDetailResponseProduct2.getProduct().getName()).isEqualTo("Capa Moto X4");
        assertThat(orderDetailResponseProduct2.getProduct().getValue()).isEqualTo(new BigDecimal("10.90"));
        assertThat(orderDetailResponseProduct2.getQuantity()).isEqualTo(2);
    }
}
