package com.cit.checkout.domain;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class OrderConfirmationBillet implements OrderConfirmation {

    @NonNull
    private BilletToPay billet;

}
