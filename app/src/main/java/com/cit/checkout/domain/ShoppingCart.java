package com.cit.checkout.domain;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ShoppingCart {

    @NonNull
    @NotBlank
    private String username;

    @NonNull
    @NotEmpty
    @Valid
    private List<PurchaseItem> items;

}
