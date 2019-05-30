package com.cit.checkout.gateway.http.assembler;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.cit.checkout.domain.Order;
import com.cit.checkout.gateway.http.json.OrderResponse;
import com.cit.checkout.gateway.http.json.PaymentBilletResponse;
import com.cit.checkout.gateway.http.json.PaymentCardResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class OrderResponseAssemblerTest {

    @Before
    public void setup() {
        FixtureFactoryLoader.loadTemplates("com.cit.checkout.templates");
    }

    @Test
    public void executeValidAssemblingToOrderResponse() {
        // GIVEN
        Order order = Fixture.from(Order.class).gimme("order 1");

        String orderIdExpected = "5435435";
        String paymentTypeExpected = "CARD";

        String transactionIdExpected = "454564235245";

        // THEN
        OrderResponse orderResponse = OrderResponseAssembler.assemble(order);

        // WHEN
        assertThat(orderResponse).isNotNull();
        assertThat(orderResponse.getOrderId()).isEqualTo(orderIdExpected);

        // AND
        PaymentCardResponse paymentCardResponse = (PaymentCardResponse) orderResponse.getPayment();

        assertThat(paymentCardResponse.getType()).isEqualTo(paymentTypeExpected);
        assertThat(paymentCardResponse.getTransactionId()).isEqualTo(transactionIdExpected);
    }

    @Test
    public void executeValidAssemblingToOrderResponseWithPaymentBillet() {
        // GIVEN
        Order order = Fixture.from(Order.class).gimme("order with billet payment");

        String orderIdExpected = "78945";
        String paymentTypeExpected = "BILLET";

        String billetNumberExpected = "6523706156860610000000 1 00099999";
        LocalDate billetDueDateExpected = LocalDate.now().plusDays(7);
        BigDecimal billetValueExpected = new BigDecimal("999.99");

        // THEN
        OrderResponse orderResponse = OrderResponseAssembler.assemble(order);

        // WHEN
        assertThat(orderResponse).isNotNull();
        assertThat(orderResponse.getOrderId()).isEqualTo(orderIdExpected);

        // AND
        PaymentBilletResponse paymentBilletResponse = (PaymentBilletResponse) orderResponse.getPayment();

        assertThat(paymentBilletResponse.getType()).isEqualTo(paymentTypeExpected);
        assertThat(paymentBilletResponse.getDueDate()).isEqualTo(billetDueDateExpected);
        assertThat(paymentBilletResponse.getNumber()).isEqualTo(billetNumberExpected);
        assertThat(paymentBilletResponse.getValue()).isEqualTo(billetValueExpected);
    }
}
