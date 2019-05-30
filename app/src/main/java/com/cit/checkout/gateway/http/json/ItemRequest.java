package com.cit.checkout.gateway.http.json;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class ItemRequest {

    @NotBlank
    @ApiModelProperty(value = "The product SKU", example = "SKU-123-123", required = true)
    private String productCode;

    @Positive
    @ApiModelProperty(value = "Amount bought", required = true, example = "2")
    private Integer quantity;

}
