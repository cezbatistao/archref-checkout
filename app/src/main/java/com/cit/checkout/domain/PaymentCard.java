package com.cit.checkout.domain;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PaymentCard extends Payment {

    @NotBlank
    private String creditCard;

    @Builder
    public PaymentCard(PaymentType type, String creditCard) {
        super(type);
        this.creditCard = creditCard;
    }
}
