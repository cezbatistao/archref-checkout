package com.cit.checkout.gateway.http;

import com.cit.checkout.conf.log.LogKey;
import com.cit.checkout.domain.Order;
import com.cit.checkout.domain.Payment;
import com.cit.checkout.domain.ShoppingCart;
import com.cit.checkout.gateway.http.assembler.UserOrdersResponseAssembler;
import com.cit.checkout.gateway.http.json.OrderRequest;
import com.cit.checkout.gateway.http.json.OrderResponse;
import com.cit.checkout.gateway.http.json.PaymentRequest;
import com.cit.checkout.gateway.http.json.UserOrdersResponse;
import com.cit.checkout.usecase.CheckoutOrder;
import com.cit.checkout.usecase.SearchUserOrders;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

import static com.cit.checkout.gateway.http.assembler.OrderResponseAssembler.assemble;
import static com.cit.checkout.gateway.http.assembler.PaymentAssembler.assemble;
import static com.cit.checkout.gateway.http.assembler.ShoppingCartAssembler.assemble;
import static net.logstash.logback.argument.StructuredArguments.value;

@Slf4j
@RestController
@Validated
@RequestMapping("/api/v1")
@Api(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    private CheckoutOrder checkoutOrder;
    private SearchUserOrders searchUserOrders;

    @Autowired
    public OrderController(CheckoutOrder checkoutOrder, SearchUserOrders searchUserOrders) {
        this.checkoutOrder = checkoutOrder;
        this.searchUserOrders = searchUserOrders;
    }

    @ApiOperation(value = "Place Order")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Order palced with success"),
                    @ApiResponse(code = 403, message = "Feature disabled"),
                    @ApiResponse(code = 408, message = "Request Timeout"),
                    @ApiResponse(code = 422, message = "Error placing order"),
                    @ApiResponse(code = 500, message = "Internal Server Error")
            }
    )
    @RequestMapping(path = "/order", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public OrderResponse placeOrder(@RequestBody @Valid OrderRequest request,
                                    @RequestHeader(value = "user") @NotBlank String customer) {
        log.info("Placing new order with {} having payment method like {}",
                value(LogKey.AMOUNT_ITEMS.toString(), request.getItems()),
                value(LogKey.PAYMENT_METHOD.toString(), Optional.ofNullable(request.getPayment())
                        .map(PaymentRequest::getType)
                        .orElse(null)));

        ShoppingCart shoppingCart = assemble(customer, request.getItems());
        Payment payment = assemble(request.getPayment());

        Order order = checkoutOrder.execute(shoppingCart, payment);

        return assemble(order);
    }

    @ApiOperation(value = "Detail Order")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "List of Orders from username"),
                    @ApiResponse(code = 403, message = "Feature disabled"),
                    @ApiResponse(code = 408, message = "Request Timeout"),
                    @ApiResponse(code = 500, message = "Internal Server Error")
            }
    )
    @RequestMapping(path = "orders/{username}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<UserOrdersResponse> listOrders(@PathVariable String username) {
        log.info("Listing all order from usename {}",
                value(LogKey.AMOUNT_ITEMS.toString(), username));

        List<Order> orders = searchUserOrders.execute(username);

        return UserOrdersResponseAssembler.assemble(orders);
    }
}
