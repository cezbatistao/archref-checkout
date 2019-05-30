package com.cit.checkout.stepdefinitions;

import br.com.six2six.fixturefactory.Fixture;
import com.cit.checkout.ApplicationConfiguration;
import com.cit.checkout.gateway.http.json.OrderRequest;
import com.cit.checkout.gateway.http.json.OrderResponse;
import com.cit.checkout.gateway.http.json.error.ErrorResponse;
import com.cit.checkout.gateway.mysql.ProductRepository;
import com.cit.checkout.gateway.mysql.StockRepository;
import com.cit.checkout.gateway.mysql.model.ProductModel;
import com.cit.checkout.gateway.mysql.model.StockModel;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(classes = ApplicationConfiguration.class)
@WebAppConfiguration
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class OrderDefinitions {

    private String username;
    private OrderRequest request;

    private Response response;
    private ValidatableResponse json;

    private StockRepository stockRepository;
    private ProductRepository productRepository;

    @Autowired
    public OrderDefinitions(StockRepository stockRepository, ProductRepository productRepository) {
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
    }

    @Given("^I have stock from product$")
    public void i_have_stock_from_product() {
        ProductModel motox4 = Fixture.from(ProductModel.class).gimme("model product moto x 4");
        StockModel stockFromMotox4 = Fixture.from(StockModel.class).gimme("model product 1 with one in stock");

        ProductModel savedMotoX4 = productRepository.save(motox4);

        stockFromMotox4.setProduct(savedMotoX4);
        stockRepository.save(stockFromMotox4);
    }

    @Given("^an order for \"([^\"]*)\"$")
    public void an_order_for(String order) {
        this.request = Fixture.from(OrderRequest.class).gimme(order);
    }

    @Given("^that is being purchased by \"([^\"]*)\"$")
    public void that_is_being_purchased_by(String username) {
        this.username = username;
    }

    @When("^I place the order$")
    public void iPlaceTheOrder() {
        this.response = given()
                .contentType("application/json")
                .header(new Header("user", this.username))
                .body(this.request)
                .post("/api/v1/order");
    }

    @Then("^the status code is (\\d+)$")
    public void verify_status_code(int statusCodeExpected){
        json = response.then()
                .statusCode(statusCodeExpected);
    }

    @And("^I receive an confirmantion from credicard$")
    public void i_receive_an_confirmation_from_credicard() {
        OrderResponse orderResponse = json.extract().as(OrderResponse.class);

//        json.assertThat()
//                .statusCode(HttpStatus.OK.value())
//                .body("orderId", notNullValue())
//                .body("payment.type", equalTo("CARD"))
//                .body("payment.transactionId", notNullValue());

        assertThat(orderResponse).isNotNull();
        assertThat(orderResponse.getOrderId()).isNotNull();
        assertThat(orderResponse.getPayment()).isNotNull();
        assertThat(orderResponse.getPayment().getType()).isEqualTo("CARD");
        assertThat(orderResponse.getPayment().getTransactionId()).isNotNull();
        assertThat(orderResponse.getPayment().getNumber()).isNull();
        assertThat(orderResponse.getPayment().getDueDate()).isNull();
        assertThat(orderResponse.getPayment().getValue()).isNull();
    }

    @And("^I receive an confirmantion with generated billet$")
    public void i_receive_an_confirmantion_with_generated_billet() {
        OrderResponse orderResponse = json.extract().as(OrderResponse.class);

//        json.assertThat()
//                .statusCode(HttpStatus.OK.value())
//                .body("orderId", notNullValue())
//                .body("payment.type", equalTo("BILLET"))
//                .body("payment.number", notNullValue())
//                .body("payment.dueDate", notNullValue())
//                .body("payment.value", notNullValue());

        assertThat(orderResponse).isNotNull();
        assertThat(orderResponse.getOrderId()).isNotNull();
        assertThat(orderResponse.getPayment()).isNotNull();
        assertThat(orderResponse.getPayment().getType()).isEqualTo("BILLET");
        assertThat(orderResponse.getPayment().getTransactionId()).isNull();
        assertThat(orderResponse.getPayment().getNumber()).isNotNull();
        assertThat(orderResponse.getPayment().getDueDate()).isNotNull();
        assertThat(orderResponse.getPayment().getValue()).isNotNull();
    }

    @Then("^I receive an error")
    public void iReceiveAnError() {
        ErrorResponse response = this.response
                .then()
                .extract()
                .as(ErrorResponse.class);

        assertThat(response).isNotNull();
    }
}
