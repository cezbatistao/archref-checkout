package com.cit.checkout.gateway.http.json;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@ToString
public class PaymentBilletResponse extends PaymentResponse {

    private String number;
    private LocalDate dueDate;
    private BigDecimal value;

    @Builder
    public PaymentBilletResponse(String type, String number, LocalDate dueDate, BigDecimal value) {
        super(type);
        this.number = number;
        this.dueDate = dueDate;
        this.value = value;
    }
}
