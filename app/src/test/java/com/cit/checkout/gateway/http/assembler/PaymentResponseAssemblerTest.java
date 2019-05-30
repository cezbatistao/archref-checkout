package com.cit.checkout.gateway.http.assembler;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.cit.checkout.domain.OrderConfirmation;
import com.cit.checkout.domain.OrderConfirmationBillet;
import com.cit.checkout.domain.OrderConfirmationCard;
import com.cit.checkout.gateway.http.json.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PaymentResponseAssemblerTest {

    @Before
    public void setup() {
        FixtureFactoryLoader.loadTemplates("com.cit.checkout.templates");
    }

    @Test
    public void executeAssemblingFromNullOrderConfirmation() {
        // GIVEN
        OrderConfirmation orderConfirmation = null;

        // THEN
        PaymentResponse paymentResponse = PaymentResponseAssembler.assemble(orderConfirmation);

        // WHEN
        assertThat(paymentResponse).isNull();
    }

    @Test
    public void executeAssemblingFromOrderConfirmationCardToPaymentCardResponse() {
        // GIVEN
        OrderConfirmation orderConfirmationCard = Fixture.from(OrderConfirmationCard.class)
                .gimme("order confirmation to credit card");

        String paymentTypeExpceted = "CARD";
        String transactionIdExpected = "454564235245";

        // THEN
        PaymentResponse paymentResponse = PaymentResponseAssembler.assemble(orderConfirmationCard);

        // WHEN
        assertThat(paymentResponse).isNotNull();
        assertThat(paymentResponse.getType()).isEqualTo(paymentTypeExpceted);

        // AND
        PaymentCardResponse paymentCardResponse = (PaymentCardResponse) paymentResponse;

        assertThat(paymentCardResponse.getTransactionId()).isEqualTo(transactionIdExpected);
    }

    @Test
    public void executeAssemblingFromOrderConfirmationBilletToPaymentBilletResponse() {
        // GIVEN
        OrderConfirmation orderConfirmationBillet = Fixture.from(OrderConfirmationBillet.class)
                .gimme("order confirmation to billet");

        String paymentTypeExpceted = "BILLET";

        String billetNumberExpected = "6523706156860610000000 1 00099999";
        String billetDueDateExpected = LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String billetValueExpected = "999.99";

        // THEN
        PaymentResponse paymentResponse = PaymentResponseAssembler.assemble(orderConfirmationBillet);

        // WHEN
        assertThat(paymentResponse).isNotNull();
        assertThat(paymentResponse.getType()).isEqualTo(paymentTypeExpceted);

        // AND
        PaymentBilletResponse paymentBilletResponse = (PaymentBilletResponse) paymentResponse;

        assertThat(paymentBilletResponse.getNumber()).isEqualTo(billetNumberExpected);
        assertThat(paymentBilletResponse.getDueDate()).isEqualTo(billetDueDateExpected);
        assertThat(paymentBilletResponse.getValue()).isEqualTo(billetValueExpected);
    }
}
