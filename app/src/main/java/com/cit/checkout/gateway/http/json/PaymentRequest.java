package com.cit.checkout.gateway.http.json;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@ToString
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true,
        defaultImpl = PaymentCardRequest.class
)
@JsonSubTypes({
        @Type(value = PaymentCardRequest.class, name = "CARD"),
        @Type(value = PaymentBilletRequest.class, name = "BILLET")
})
@ApiModel(discriminator = "type", subTypes = { PaymentCardRequest.class, PaymentBilletRequest.class })
public abstract class PaymentRequest {

    @NotBlank
    @ApiModelProperty(value = "The payment method", required = true, example = "CARD")
    private String type;

}
