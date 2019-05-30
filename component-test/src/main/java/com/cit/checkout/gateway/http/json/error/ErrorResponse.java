package com.cit.checkout.gateway.http.json.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ErrorResponse implements Serializable {

    private Integer status;
    private String error;
    private String description;

    private List<ErrorField> errorFields;
}
