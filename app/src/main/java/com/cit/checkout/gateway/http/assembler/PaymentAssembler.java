package com.cit.checkout.gateway.http.assembler;

import com.cit.checkout.domain.Payment;
import com.cit.checkout.domain.PaymentBillet;
import com.cit.checkout.domain.PaymentCard;
import com.cit.checkout.domain.PaymentType;
import com.cit.checkout.gateway.http.json.PaymentCardRequest;
import com.cit.checkout.gateway.http.json.PaymentRequest;

import java.util.Optional;

public class PaymentAssembler {

    private PaymentAssembler() {
    }

    public static Payment assemble(PaymentRequest paymentRequestToAssembler) {
        return Optional.ofNullable(paymentRequestToAssembler)
                .map(paymentRequest -> {
                    PaymentType paymentType = PaymentType.valueOf(paymentRequest.getType());
                    if(paymentType.equals(PaymentType.BILLET)) {
                        return PaymentBillet.builder()
                                .type(PaymentType.BILLET)
                                .build();
                    } else {
                        return PaymentCard.builder()
                                .type(PaymentType.CARD)
                                .creditCard(((PaymentCardRequest) paymentRequest).getCardValue())
                                .build();
                    }
                })
                .orElse(null);
    }
}
