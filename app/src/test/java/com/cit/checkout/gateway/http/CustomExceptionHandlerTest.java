package com.cit.checkout.gateway.http;

import com.cit.checkout.gateway.conf.AbstractHttpTest;
import com.cit.checkout.gateway.http.json.error.StatusError;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CustomExceptionHandlerTestController.class)
public class CustomExceptionHandlerTest extends AbstractHttpTest {

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = buildMockMvcWithBusinessExecptionHandler();
    }

    @Test
    public void testMethodNotSupported() throws Exception {
        // GIVEN
        StatusError methodNotAllowedErrorExpected = StatusError.METHOD_NOT_ALLOWED;

        // THEN
        mockMvc
                .perform(post("/test/method-not-supported"))
                .andExpect(status().isMethodNotAllowed())

        // WHEN
                .andExpect(jsonPath("$.status").value(methodNotAllowedErrorExpected.getStatus()))
                .andExpect(jsonPath("$.error").value(methodNotAllowedErrorExpected.getMessage()))
                .andExpect(jsonPath("$.description").value("Request method 'POST' not supported"))
                .andExpect(jsonPath("$.errorFields").doesNotExist());
    }

    @Test
    public void testMethodnotPaid() throws Exception {
        // GIVEN
        StatusError validationErrorExpected = StatusError.VALIDATION;

        // THEN
        mockMvc
                .perform(get("/test/not-paid"))
                .andExpect(status().isUnprocessableEntity())

        // WHEN
                .andExpect(jsonPath("$.status").value(validationErrorExpected.getStatus()))
                .andExpect(jsonPath("$.error").value(validationErrorExpected.getMessage()))
                .andExpect(jsonPath("$.description").value("Error processing order checkout"))
                .andExpect(jsonPath("$.errorFields").doesNotExist());
    }

    @Test
    public void testInternalServerError() throws Exception {
        // GIVEN
        StatusError internalServerErrorExpected = StatusError.INTERNAL_SERVER_ERROR;

        // THEN
        mockMvc
                .perform(get("/test/internal-server-error"))

        // WHEN
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(internalServerErrorExpected.getStatus()))
                .andExpect(jsonPath("$.error").value(internalServerErrorExpected.getMessage()))
                .andExpect(jsonPath("$.description").doesNotExist())
                .andExpect(jsonPath("$.errorFields").doesNotExist());
    }

    @Test
    public void testIllegalArgumentError() throws Exception {
        // GIVEN
        StatusError invalidArgumentErrorExpected = StatusError.INVALID_ARGUMENT;

        // THEN
        mockMvc
                .perform(get("/test/illegal-argument-error"))

        // WHEN
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(invalidArgumentErrorExpected.getStatus()))
                .andExpect(jsonPath("$.error").value(invalidArgumentErrorExpected.getMessage()))
                .andExpect(jsonPath("$.description").doesNotExist())
                .andExpect(jsonPath("$.errorFields").doesNotExist());
    }

    @Test
    public void testValidationError() throws Exception {
        // GIVEN
        StatusError validationErrorExpected = StatusError.VALIDATION;

        // THEN
        mockMvc
                .perform(get("/test/validation-error"))

        // WHEN
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(validationErrorExpected.getStatus()))
                .andExpect(jsonPath("$.error").value(validationErrorExpected.getMessage()))
                .andExpect(jsonPath("$.description").doesNotExist())
                .andExpect(jsonPath("$.errorFields").doesNotExist());
    }

    @Test
    public void testValidationWithFieldsError() throws Exception {
        // GIVEN
        StatusError validationErrorExpected = StatusError.VALIDATION;

        // THEN
        mockMvc
                .perform(get("/test/validation-with-fields-error"))

        // WHEN
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(validationErrorExpected.getStatus()))
                .andExpect(jsonPath("$.error").value(validationErrorExpected.getMessage()))
                .andExpect(jsonPath("$.description").value("error.validationFields"))
                .andExpect(jsonPath("$.errorFields").isArray())
                .andExpect(jsonPath("$.errorFields", hasSize(2)))
                .andExpect(jsonPath("$.errorFields[*].messageCode",
                        containsInAnyOrder("{javax.validation.constraints.NotBlank.message}", "{javax.validation.constraints.Min.message}")))
                .andExpect(jsonPath("$.errorFields[*].message",
                        containsInAnyOrder("must not be blank", "must be greater than or equal to 0")))
                .andExpect(jsonPath("$.errorFields[0].fields").isArray())
                .andExpect(jsonPath("$.errorFields[0].fields", hasSize(2)))
                .andExpect(jsonPath("$.errorFields[0].fields", hasItems("field1", "field2")))
                .andExpect(jsonPath("$.errorFields[1].fields").isArray())
                .andExpect(jsonPath("$.errorFields[1].fields", hasSize(1)))
                .andExpect(jsonPath("$.errorFields[1].fields", hasItem("field3")));
    }

    @Test
    public void testFeatureDisabled() throws Exception {
        // GIVEN
        StatusError featureDisabledErrorExpected = StatusError.FEATURE_DISABLED;

        // THEN
        mockMvc
                .perform(get("/test/feature-disabled"))

        // WHEN
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(featureDisabledErrorExpected.getStatus()))
                .andExpect(jsonPath("$.error").value(featureDisabledErrorExpected.getMessage()))
                .andExpect(jsonPath("$.description").value("Feature disabled: billet-payment"))
                .andExpect(jsonPath("$.errorFields").doesNotExist());
    }

    @Test
    public void testStockWithoutProduct() throws Exception {
        // GIVEN
        StatusError featureDisabledErrorExpected = StatusError.VALIDATION;

        // THEN
        mockMvc
                .perform(get("/test/stock-without-product"))

                // WHEN
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(featureDisabledErrorExpected.getStatus()))
                .andExpect(jsonPath("$.error").value(featureDisabledErrorExpected.getMessage()))
                .andExpect(jsonPath("$.description").value("Product [SKU-321-321] without enough in stock, has: 1"))
                .andExpect(jsonPath("$.errorFields").doesNotExist());
    }

    @Test
    public void testMethodArgumentNotValid() throws Exception {
        // GIVEN
        StatusError invalidArgumentErrorExpected = StatusError.INVALID_ARGUMENT;

        // THEN
        mockMvc
                .perform(
                        post("/test/method-argument").content("{}").contentType(MediaType.APPLICATION_JSON))

        // WHEN
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(invalidArgumentErrorExpected.getStatus()))
                .andExpect(jsonPath("$.error").value(invalidArgumentErrorExpected.getMessage()))
                .andExpect(jsonPath("$.description").doesNotExist())
                .andExpect(jsonPath("$.errorFields.[0].messageCode").value("NotNull"))
                .andExpect(jsonPath("$.errorFields.[0].fields.[0]").value("test"));
    }

    @Test
    public void testIntegrationPaymentError() throws Exception {
        // GIVEN
        StatusError validationExpected = StatusError.VALIDATION;

        // THEN
        mockMvc
                .perform(get("/test/integration-airport-error"))

        // WHEN
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(validationExpected.getStatus()))
                .andExpect(jsonPath("$.error").value(validationExpected.getMessage()))
                .andExpect(jsonPath("$.description").value("Error payment process"))
                .andExpect(jsonPath("$.errorFields").doesNotExist());
    }

    @Test
    public void testEntityNotFoundError() throws Exception {
        // GIVEN
        StatusError invalidArgumentExpected = StatusError.INVALID_ARGUMENT;

        // THEN
        mockMvc
                .perform(get("/test/entity-not-found-error"))

                // WHEN
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(invalidArgumentExpected.getStatus()))
                .andExpect(jsonPath("$.error").value(invalidArgumentExpected.getMessage()))
                .andExpect(jsonPath("$.description").value("Order Not Found"))
                .andExpect(jsonPath("$.errorFields").isArray())
                .andExpect(jsonPath("$.errorFields", hasSize(1)))
                .andExpect(jsonPath("$.errorFields.[0].messageCode").value("order.dontExists"))
                .andExpect(jsonPath("$.errorFields.[0].message").value("Order Not Found"))
                .andExpect(jsonPath("$.errorFields.[0].fields.[0]").value("order"));
    }

    @Test
    public void testHystrixFallBackGenericError() throws Exception {
        // GIVEN
        StatusError internalServerErrorExpected = StatusError.INTERNAL_SERVER_ERROR;

        // THEN
        mockMvc
                .perform(get("/test/fallback-generic-error"))

        // WHEN
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(internalServerErrorExpected.getStatus()))
                .andExpect(jsonPath("$.error").value(internalServerErrorExpected.getMessage()))
                .andExpect(jsonPath("$.description").doesNotExist())
                .andExpect(jsonPath("$.errorFields").doesNotExist());
    }
}
