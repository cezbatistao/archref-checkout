package com.cit.checkout.domain;

import lombok.*;

import java.time.LocalDate;

@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@ToString(callSuper = true)
public class PaymentBillet extends Payment {

    // TODO colocar esses campos
//    private String assignor;
//    private String drawee;
//    private Double documentValue;
//    private LocalDate dueDate;
//    private String barcode;

    @Builder
    public PaymentBillet(PaymentType type) {
        super(type);
    }
}
