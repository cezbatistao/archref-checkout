package com.cit.checkout.domain.error;

import lombok.*;

@Data
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConstraintError {

    private String message;
    private String messageTemplate;
    private String propertyPath;
    private Object invalidValue;

}
