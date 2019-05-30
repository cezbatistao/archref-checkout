package com.cit.checkout.gateway.http.assembler;

import com.cit.checkout.domain.OrderConfirmation;
import com.cit.checkout.domain.OrderConfirmationBillet;
import com.cit.checkout.domain.OrderConfirmationCard;
import com.cit.checkout.gateway.http.json.PaymentBilletResponse;
import com.cit.checkout.gateway.http.json.PaymentCardResponse;
import com.cit.checkout.gateway.http.json.PaymentResponse;

public class PaymentResponseAssembler {

    private static final String PAYMENT_TYPE_CARD = "CARD";
    private static final String PAYMENT_TYPE_BILLET = "BILLET";

    private PaymentResponseAssembler() {
    }

    public static PaymentResponse assemble(OrderConfirmation orderConfirmation) {
        if(orderConfirmation instanceof OrderConfirmationCard) {
            return assemble((OrderConfirmationCard) orderConfirmation);
        } else if(orderConfirmation instanceof OrderConfirmationBillet) {
            return assemble((OrderConfirmationBillet) orderConfirmation);
        }

        return null;
    }

    public static PaymentResponse assemble(OrderConfirmationCard orderConfirmationCard) {
        return PaymentCardResponse.builder()
                .type(PAYMENT_TYPE_CARD)
                .transactionId(orderConfirmationCard.getTransactionId())
                .build();
    }

    public static PaymentResponse assemble(OrderConfirmationBillet orderConfirmationBillet) {
        return PaymentBilletResponse.builder()
                .type(PAYMENT_TYPE_BILLET)
                .number(orderConfirmationBillet.getBillet().getNumber())
                .dueDate(orderConfirmationBillet.getBillet().getDueDate())
                .value(orderConfirmationBillet.getBillet().getValue())
                .build();
    }
}
