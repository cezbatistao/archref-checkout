package com.cit.checkout.gateway;

import com.cit.checkout.domain.PaymentCard;
import com.cit.checkout.domain.PaymentStatus;

public interface PaymentGateway {

    PaymentStatus process(PaymentCard payment, double total);

}
