package com.cit.checkout.domain;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class PurchaseItem {

    @NonNull
    @NotBlank
    private String productCode;

    @NonNull
    @Min(1)
    @NotNull
    private Integer quantity;

}
