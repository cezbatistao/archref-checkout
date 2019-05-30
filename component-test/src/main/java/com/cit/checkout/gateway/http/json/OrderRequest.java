package com.cit.checkout.gateway.http.json;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderRequest implements Serializable {

    private static final long serialVersionUID = 2415328239342194229L;

    private List<ItemRequest> items;
    private PaymentRequest payment;

}
