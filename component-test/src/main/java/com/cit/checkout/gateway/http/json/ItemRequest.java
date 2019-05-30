package com.cit.checkout.gateway.http.json;

import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequest implements Serializable {

    private static final long serialVersionUID = -1072553853673516217L;

    private String productCode;
    private Integer quantity;

}
