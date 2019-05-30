package com.cit.checkout.gateway.http.json;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@ApiModel
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class OrderRequest implements Serializable {

    private static final long serialVersionUID = 2415328239342194229L;

    @Valid
    @NotEmpty
    @ApiModelProperty(value = "Order items", required = true)
    private List<ItemRequest> items;

    @Valid
    @NotNull
    @ApiModelProperty(value = "Order PaymentRequest method", required = true)
    private PaymentRequest payment;

}
