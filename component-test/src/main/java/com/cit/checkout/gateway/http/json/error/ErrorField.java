package com.cit.checkout.gateway.http.json.error;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ErrorField {

    String messageCode;
    String message;
    List<String> fields;

}
