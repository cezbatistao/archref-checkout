package com.cit.checkout.gateway.http;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.cit.checkout.domain.Order;
import com.cit.checkout.domain.exception.NotPaidException;
import com.cit.checkout.gateway.conf.AbstractHttpTest;
import com.cit.checkout.gateway.http.json.OrderRequest;
import com.cit.checkout.gateway.http.json.error.StatusError;
import com.cit.checkout.usecase.CheckoutOrder;
import com.cit.checkout.usecase.SearchUserOrders;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
public class OrderControllerTest extends AbstractHttpTest {

    private MockMvc mockMvc;

    @MockBean
    private CheckoutOrder checkoutOrder;

    @MockBean
    private SearchUserOrders searchUserOrders;

    @Before
    public void setup() {
        mockMvc = buildMockMvcWithBusinessExecptionHandler();
        FixtureFactoryLoader.loadTemplates("com.cit.checkout.templates");
    }

    @Test
    public void returnBadRequestWhenWithoutUser() throws Exception {
        //GIVEN an valid Request
        Order order = Fixture.from(Order.class).gimme("order 1");
        when(checkoutOrder.execute(any(), any())).thenReturn(order);

        OrderRequest validOrder = Fixture.from(OrderRequest.class).gimme("Valid order with VISA");

        StatusError statusErrorExpected = StatusError.INVALID_ARGUMENT;

        //WHEN the controller is called with these request params
        mockMvc
                .perform(
                        post("/api/v1/order")
                                .content(this.asJsonString(validOrder))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))

        //THEN no errors are thrown and the status returned is OK
                .andExpect(status().isBadRequest())

        // AND
                .andExpect(jsonPath("$.status").value(statusErrorExpected.getStatus()))
                .andExpect(jsonPath("$.error").value(statusErrorExpected.getMessage()))
                .andExpect(jsonPath("$.description").value("Missing request header 'user' for method parameter of type String"))
                .andExpect(jsonPath("$.errorFields").doesNotExist());
    }

    @Test
    public void returnBadRequestWhenWithoutUserIsBlank() throws Exception {
        //GIVEN an valid Request
        Order order = Fixture.from(Order.class).gimme("order 1");
        when(checkoutOrder.execute(any(), any())).thenReturn(order);

        OrderRequest validOrder = Fixture.from(OrderRequest.class).gimme("Valid order with VISA");

        StatusError statusErrorExpected = StatusError.INVALID_ARGUMENT;

        //WHEN the controller is called with these request params
        mockMvc
                .perform(
                        post("/api/v1/order")
                                .content(this.asJsonString(validOrder))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("user", " "))

        //THEN no errors are thrown and the status returned is OK
                .andExpect(status().isBadRequest())

        // AND
                .andExpect(jsonPath("$.status").value(statusErrorExpected.getStatus()))
                .andExpect(jsonPath("$.error").value(statusErrorExpected.getMessage()))
                .andExpect(jsonPath("$.description").doesNotExist())
                .andExpect(jsonPath("$.errorFields").isArray())
                .andExpect(jsonPath("$.errorFields", hasSize(1)))
                .andExpect(jsonPath("$.errorFields.[0].messageCode").value("{javax.validation.constraints.NotBlank.message}"))
                .andExpect(jsonPath("$.errorFields.[0].message").value("must not be blank"))
                .andExpect(jsonPath("$.errorFields.[0].fields.[0]").value("placeOrder.customer"));
    }

    @Test
    public void returnBadRequestWhenInvalidRequest() throws Exception {
        //GIVEN an valid Request
        Order order = Fixture.from(Order.class).gimme("order 1");
        when(checkoutOrder.execute(any(), any())).thenReturn(order);

        OrderRequest invalidOrder = Fixture.from(OrderRequest.class).gimme("Invalid order with Credicard");

        StatusError statusErrorExpected = StatusError.INVALID_ARGUMENT;

        //WHEN the controller is called with these request params
        mockMvc
                .perform(
                        post("/api/v1/order")
                                .content(this.asJsonString(invalidOrder))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("user", "someuser"))

                //THEN no errors are thrown and the status returned is OK
                .andExpect(status().isBadRequest())

                // AND
                .andExpect(jsonPath("$.status").value(statusErrorExpected.getStatus()))
                .andExpect(jsonPath("$.error").value(statusErrorExpected.getMessage()))
                .andExpect(jsonPath("$.description").doesNotExist())
                .andExpect(jsonPath("$.errorFields").isArray())
                .andExpect(jsonPath("$.errorFields", hasSize(2)))
                .andExpect(jsonPath("$.errorFields[*].messageCode",
                        containsInAnyOrder("Positive", "NotBlank")))
                .andExpect(jsonPath("$.errorFields[*].message",
                        containsInAnyOrder("must not be blank", "must be greater than 0")))
                .andExpect(jsonPath("$.errorFields[0].fields").isArray())
                .andExpect(jsonPath("$.errorFields[0].fields", hasSize(1)))
                .andExpect(jsonPath("$.errorFields[0].fields", hasItem("items[0].quantity")))
                .andExpect(jsonPath("$.errorFields[1].fields").isArray())
                .andExpect(jsonPath("$.errorFields[1].fields", hasSize(2)))
                .andExpect(jsonPath("$.errorFields[1].fields",
                        hasItems("items[0].productCode","payment.cardValue")));
    }

    @Test
    public void returnOkWhenValidRequestWithCredicard() throws Exception {
        //GIVEN an valid Request
        Order order = Fixture.from(Order.class).gimme("order 1");
        when(checkoutOrder.execute(any(), any())).thenReturn(order);

        OrderRequest validOrder = Fixture.from(OrderRequest.class).gimme("Valid order with VISA");

        String orderIdExpected = order.getOrderNumber().toString();
        String paymentTypeExpected = "CARD";
        String transactionIdExpected = "454564235245";

        mockMvc
                .perform(
                        post("/api/v1/order")
                                .content(this.asJsonString(validOrder))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("user", "someuser"))

        //THEN no errors are thrown and the status returned is OK
                .andExpect(status().isOk())

        // AND
                .andExpect(jsonPath("$.orderId").value(orderIdExpected))
                .andExpect(jsonPath("$.payment").exists())
                .andExpect(jsonPath("$.payment.type").value(paymentTypeExpected))
                .andExpect(jsonPath("$.payment.transactionId").value(transactionIdExpected));
    }

    @Test
    public void returnOkWhenValidRequestWithBillet() throws Exception {
        //GIVEN an valid Request
        Order order = Fixture.from(Order.class).gimme("order with billet payment");
        when(checkoutOrder.execute(any(), any())).thenReturn(order);

        OrderRequest validOrder = Fixture.from(OrderRequest.class).gimme("Valid order with VISA");

        String orderIdExpected = order.getOrderNumber().toString();
        String paymentTypeExpected = "BILLET";

        String billetNumberExpected = "6523706156860610000000 1 00099999";
        String billetDueDateExpected = LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String billetValueExpected = "999.99";

        //WHEN the controller is called with these request params
        mockMvc
                .perform(
                        post("/api/v1/order")
                                .content(this.asJsonString(validOrder))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("user", "someuser"))

        //THEN no errors are thrown and the status returned is OK
                .andExpect(status().isOk())

        // AND
                .andExpect(jsonPath("$.orderId").value(orderIdExpected))
                .andExpect(jsonPath("$.payment").exists())
                .andExpect(jsonPath("$.payment.type").value(paymentTypeExpected))
                .andExpect(jsonPath("$.payment.number").value(billetNumberExpected))
                .andExpect(jsonPath("$.payment.dueDate").value(billetDueDateExpected))
                .andExpect(jsonPath("$.payment.value").value(billetValueExpected));
    }

    @Test
    public void returnErrorWheninvalidItemRequest() throws Exception {
        //GIVEN an valid Request
        OrderRequest validOrder = Fixture.from(OrderRequest.class).gimme("Invalid order with VISA");

        when(checkoutOrder.execute(any(), any())).thenThrow(Exception.class);

        StatusError statusErrorExpected = StatusError.INVALID_ARGUMENT;

        //WHEN the controller is called with these request params
        mockMvc
                .perform(
                        post("/api/v1/order")
                                .content(this.asJsonString(validOrder))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))

        //THEN no errors are thrown and the status returned is NOT OK
                .andExpect(status().isBadRequest())
        // AND
                .andExpect(jsonPath("$.status").value(statusErrorExpected.getStatus()))
                .andExpect(jsonPath("$.error").value(statusErrorExpected.getMessage()))
                .andExpect(jsonPath("$.description").doesNotExist())
                .andExpect(jsonPath("$.errorFields").isArray());
    }

    @Test
    public void returnErrorWheninvalidPaymentRequest() throws Exception {
        //GIVEN an valid Request
        OrderRequest validOrder = Fixture.from(OrderRequest.class).gimme("Invalid order with no payment");

        when(checkoutOrder.execute(any(), any())).thenThrow(IllegalArgumentException.class);

        //WHEN the controller is called with these request params
        mockMvc
                .perform(
                        post("/api/v1/order")
                                .content(this.asJsonString(validOrder))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))

        //THEN
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void testUnauthorizedCreditCard() throws Exception {
        //GIVEN an valid Request
        OrderRequest validOrder = Fixture.from(OrderRequest.class).gimme("Invalid order with no payment");

        when(checkoutOrder.execute(any(), any())).thenThrow(NotPaidException.class);

        //WHEN the controller is called with these request params
        mockMvc
                .perform(
                        post("/api/v1/order")
                                .content(this.asJsonString(validOrder))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("user", "someuser"))

                //THEN
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void returnListOfOrdersFromUsername() throws Exception {
        //GIVEN an valid Request
        List<Order> orders = Fixture.from(Order.class).gimme(2, "order 1", "order 2");

        String[] ordersNumberExpected = orders.stream()
                .map(Order::getOrderNumber)
                .map(Object::toString)
                .toArray(String[]::new);

        when(searchUserOrders.execute(any())).thenReturn(orders);

        //WHEN the controller is called with these request params
        ResultActions result = mockMvc
                .perform(
                        get("/api/v1/orders/carlosz")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))

                //THEN no errors are thrown and the status returned is OK
                .andExpect(status().isOk());

        // AND
        result
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].orderNumber", contains(ordersNumberExpected)))
                .andExpect(jsonPath("$[*].value", contains(999.99, 21.80)))
                .andExpect(jsonPath("$[0].orderDetails").isArray())
                .andExpect(jsonPath("$[0].orderDetails", hasSize(1)))
                .andExpect(jsonPath("$[0].orderDetails[0].product.name").value("Moto X 4º"))
                .andExpect(jsonPath("$[0].orderDetails[0].product.code").value("SKU-123-123"))
                .andExpect(jsonPath("$[0].orderDetails[0].product.value").value(999.99))
                .andExpect(jsonPath("$[0].orderDetails[0].quantity").value(1))
                .andExpect(jsonPath("$[0].orderDetails[0].value").value(999.99))
                .andExpect(jsonPath("$[1].orderDetails").isArray())
                .andExpect(jsonPath("$[1].orderDetails", hasSize(1)))
                .andExpect(jsonPath("$[1].orderDetails[0].product.name").value("Capa Moto X4"))
                .andExpect(jsonPath("$[1].orderDetails[0].product.code").value("SKU-321-321"))
                .andExpect(jsonPath("$[1].orderDetails[0].product.value").value(10.90))
                .andExpect(jsonPath("$[1].orderDetails[0].quantity").value(2))
                .andExpect(jsonPath("$[1].orderDetails[0].value").value(10.9));
    }

    @Test
    public void returnEmptyListOfOrdersFromUsername() throws Exception {
        //GIVEN an valid Request
        when(searchUserOrders.execute(any())).thenReturn(newArrayList());

        //WHEN the controller is called with these request params
        ResultActions result = mockMvc
                .perform(
                        get("/api/v1/orders/carlosz")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))

        //THEN no errors are thrown and the status returned is OK
                .andExpect(status().isOk());

        // AND
        result
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", empty()));
    }
}
