package com.cit.checkout.gateway.http.json;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class PaymentResponse implements Serializable {

    private static final long serialVersionUID = 4043735336666071205L;

    private String type;

    // CARD type
    private String transactionId;

    // BILLET type
    private String number;
    private LocalDate dueDate;
    private BigDecimal value;
}
