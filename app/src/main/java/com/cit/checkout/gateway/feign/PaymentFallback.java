package com.cit.checkout.gateway.feign;

import com.cit.checkout.gateway.feign.json.PaymentRequest;
import com.cit.checkout.gateway.feign.json.PaymentResponse;
import com.cit.checkout.gateway.feign.json.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public class PaymentFallback implements PaymentClient {

    @Override
    public PaymentResponse process(PaymentRequest request){
        PaymentResponse response = new PaymentResponse();
        response.setStatus(PaymentStatus.NOT_SENT);
        response.setTransactionId("internal-12345");
        response.setDetails("");
        return response;
    }
}
