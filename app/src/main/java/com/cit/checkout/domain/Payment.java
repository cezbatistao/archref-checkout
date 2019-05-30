package com.cit.checkout.domain;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
public abstract class Payment {

    @NotNull
    private PaymentType type;

}
