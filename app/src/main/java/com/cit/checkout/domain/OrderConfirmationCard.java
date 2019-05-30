package com.cit.checkout.domain;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class OrderConfirmationCard implements OrderConfirmation {

    @NotNull
    private String transactionId;

}
