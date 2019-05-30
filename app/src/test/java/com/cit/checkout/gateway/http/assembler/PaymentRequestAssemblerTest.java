package com.cit.checkout.gateway.http.assembler;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.cit.checkout.domain.Payment;
import com.cit.checkout.domain.PaymentBillet;
import com.cit.checkout.domain.PaymentCard;
import com.cit.checkout.domain.PaymentType;
import com.cit.checkout.gateway.http.json.PaymentBilletRequest;
import com.cit.checkout.gateway.http.json.PaymentCardRequest;
import com.cit.checkout.gateway.http.json.PaymentRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PaymentRequestAssemblerTest {

    @Before
    public void setup() {
        FixtureFactoryLoader.loadTemplates("com.cit.checkout.templates");
    }

    @Test
    public void executeValidAssemblingToPaymentCardFromPaymentRequest() {
        // GIVEN
        PaymentRequest paymentRequest = Fixture.from(PaymentCardRequest.class).gimme("MasterCard");

        // WHEN
        Payment payment = PaymentAssembler.assemble(paymentRequest);

        // THEN
        assertThat(payment).isNotNull();
        assertThat(payment).isExactlyInstanceOf(PaymentCard.class);

        PaymentCard paymentCard = (PaymentCard) payment;

        assertThat(paymentCard.getType()).isEqualTo(PaymentType.CARD);
        assertThat(paymentCard.getCreditCard()).isEqualTo("5105105105105100");
    }

    @Test
    public void executeValidAssemblingToPaymentBilletFromPaymentRequest() {
        // GIVEN
        PaymentRequest paymentRequest = Fixture.from(PaymentBilletRequest.class).gimme("BILLET");

        // WHEN
        Payment payment = PaymentAssembler.assemble(paymentRequest);

        // THEN
        assertThat(payment).isNotNull();
        assertThat(payment).isExactlyInstanceOf(PaymentBillet.class);

        PaymentBillet paymentBillet = (PaymentBillet) payment;

        assertThat(paymentBillet.getType()).isEqualTo(PaymentType.BILLET);
    }
}
