package com.cit.checkout.gateway.feign;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.cit.checkout.domain.PaymentCard;
import com.cit.checkout.domain.PaymentStatus;
import com.cit.checkout.domain.TransactionStatus;
import com.cit.checkout.gateway.feign.json.PaymentResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
public class PaymentGatewayImplTest {


    @Mock
    private PaymentClient paymentClient;

    @InjectMocks
    private PaymentGatewayImpl paymentGateway;


    @Before
    public void setup() {
        FixtureFactoryLoader.loadTemplates("com.cit.checkout.templates");
    }


    @Test
    public void validPaymentWithPaidResponse() throws Exception {
        //GIVEN
        PaymentCard request = Fixture.from(PaymentCard.class).gimme("mastercard");
        PaymentResponse response = Fixture.from(PaymentResponse.class).gimme("PAID");

        when(paymentClient.process(any())).thenReturn(response);

        //WHEN:
        PaymentStatus payment = paymentGateway.process(request, 2000);

        //THEN:
        verify(paymentClient, times(1)).process(any());

        assertThat(payment).isNotNull();
        assertThat(payment.getStatus()).isEqualTo(TransactionStatus.PAID);
    }

    @Test
    public void validPaymentWithFraudResponse() throws Exception {
        //GIVEN
        PaymentCard request = Fixture.from(PaymentCard.class).gimme("mastercard");
        PaymentResponse response = Fixture.from(PaymentResponse.class).gimme("FRAUD");

        when(paymentClient.process(any())).thenReturn(response);

        //WHEN:
        PaymentStatus payment = paymentGateway.process(request, 2000);

        //THEN:
        verify(paymentClient, times(1)).process(any());

        assertThat(payment).isNotNull();
        assertThat(payment.getStatus()).isEqualTo(TransactionStatus.FRAUD);
    }

    @Test
    public void validPaymentWithNotEnouthMoneyResponse() throws Exception {
        //GIVEN
        PaymentCard request = Fixture.from(PaymentCard.class).gimme("mastercard");
        PaymentResponse response = Fixture.from(PaymentResponse.class).gimme("INSUFFICIENT_FUNDS");

        when(paymentClient.process(any())).thenReturn(response);

        //WHEN:
        PaymentStatus payment = paymentGateway.process(request, 2000);

        //THEN:
        verify(paymentClient, times(1)).process(any());

        assertThat(payment).isNotNull();
        assertThat(payment.getStatus()).isEqualTo(TransactionStatus.INSUFFICIENT_FUNDS);
    }
}
