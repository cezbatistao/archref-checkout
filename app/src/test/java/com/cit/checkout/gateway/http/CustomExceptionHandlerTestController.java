package com.cit.checkout.gateway.http;

import com.cit.checkout.conf.ff4j.Features;
import com.cit.checkout.domain.Product;
import com.cit.checkout.domain.error.ConstraintError;
import com.cit.checkout.domain.exception.*;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@RestController
public class CustomExceptionHandlerTestController {

    @GetMapping("/test/method-not-supported")
    public void methodNotSupported() {
    }

    @GetMapping("/test/internal-server-error")
    public void internalServerError() {
        throw new RuntimeException();
    }

    @GetMapping("/test/illegal-argument-error")
    public void illegalArgumentError() {
        throw new IllegalArgumentException();
    }

    @GetMapping("/test/validation-error")
    public void validationError() {
        throw new ValidationException(null);
    }

    @GetMapping("/test/validation-with-fields-error")
    public void validationWithFieldsError() {
        List<ConstraintError> errors = newArrayList(
                ConstraintError.builder()
                        .message("must not be blank")
                        .messageTemplate("{javax.validation.constraints.NotBlank.message}")
                        .propertyPath("field1")
                        .build(),
                ConstraintError.builder()
                        .message("must not be blank")
                        .messageTemplate("{javax.validation.constraints.NotBlank.message}")
                        .propertyPath("field2")
                        .build(),
                ConstraintError.builder()
                        .message("must be greater than or equal to 0")
                        .messageTemplate("{javax.validation.constraints.Min.message}")
                        .propertyPath("field3")
                        .build());

        throw new ValidationException("error.validationFields", errors);
    }

    @GetMapping("/test/entity-not-found-error")
    public void entityNotFoundException() {
        throw EntityNotFoundException.createOrderNotFoundException("Order Not Found");
    }

    @GetMapping("/test/integration-airport-error")
    public void integrationAirportError() {
        throw new ValidationException("Error payment process");
    }

    @GetMapping("/test/fallback-generic-error")
    public void fallbackGenericError() {
        throw new HystrixRuntimeException(
                HystrixRuntimeException.FailureType.BAD_REQUEST_EXCEPTION,
                null,
                null,
                null,
                new Exception("", new Exception("", new RuntimeException("Generic Exception"))));
    }

    @GetMapping("/test/feature-disabled")
    public void featureDisabled() {
        throw new FeatureException(Features.BILLET_PAYMENT, "test");
    }

    @GetMapping("/test/stock-without-product")
    public void stockWithoutProduct() {
        throw new StockWithoutProductException(Product.builder()
                .id(1L)
                .name("Product 1")
                .code("SKU-321-321")
                .value(new BigDecimal("10.90"))
                .build(), 1,
                "Product [SKU-321-321] without enough in stock, has: 1");
    }

    @GetMapping("/test/not-paid")
    public void notPaid() {
        throw new NotPaidException("A unprocessable checkout error");
    }

    @PostMapping("/test/method-argument")
    public void methodArgument(@Valid @RequestBody TestDTO testDTO) {
    }

    public static class TestDTO {

        @NotNull
        private String test;

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }
    }
}
