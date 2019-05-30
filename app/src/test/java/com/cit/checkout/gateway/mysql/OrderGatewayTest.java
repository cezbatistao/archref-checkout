package com.cit.checkout.gateway.mysql;

import com.cit.checkout.domain.Order;
import com.cit.checkout.domain.Product;
import com.cit.checkout.domain.Purchase;
import com.cit.checkout.domain.exception.EntityNotFoundException;
import com.cit.checkout.gateway.OrderGateway;
import com.cit.checkout.gateway.conf.MySQLDBRepositoryTest;
import com.cit.checkout.gateway.mysql.model.OrderModel;
import com.cit.checkout.gateway.mysql.model.ProductModel;
import com.cit.checkout.gateway.mysql.model.PurchaseModel;
import com.cit.checkout.templates.processor.PurchaseAjustProductIdProcessor;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.six2six.fixturefactory.Fixture.from;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class OrderGatewayTest extends MySQLDBRepositoryTest {

    @Autowired
    private OrderGateway orderGateway;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    private ProductModel productModelCellphoneCaseMotoX4;

    private OrderModel orderModelFromProductMotoX4;

    @Before
    public void setUp() {
        ProductModel productModelMotoX4 = from(ProductModel.class).gimme("model product moto x 4");
        productModelMotoX4 = productRepository.save(productModelMotoX4);

        productModelCellphoneCaseMotoX4 = from(ProductModel.class).gimme("model product cellphone case moto x 4");
        productModelCellphoneCaseMotoX4 = productRepository.save(productModelCellphoneCaseMotoX4);

        orderModelFromProductMotoX4 = from(OrderModel.class).gimme("order model 1");
        PurchaseModel purchaseModelFromProductMotoX4 = orderModelFromProductMotoX4.getItems().get(0);
        purchaseModelFromProductMotoX4.setProduct(productModelMotoX4);
        orderModelFromProductMotoX4 = orderRepository.save(orderModelFromProductMotoX4);

        OrderModel orderModelFromProductCaseMotoX4 = from(OrderModel.class).gimme("order model 2");
        PurchaseModel purchaseModelFromProductCaseMotoX4 = orderModelFromProductCaseMotoX4.getItems().get(0);
        purchaseModelFromProductCaseMotoX4.setProduct(productModelCellphoneCaseMotoX4);
        orderRepository.save(orderModelFromProductCaseMotoX4);
    }

    @Test
    public void testUsernameWithoutOrders() {
        // GIVEN
        String username = "eduardoz";

        // WHEN
        List<Order> orders = orderGateway.findAllByUsername(username);

        // THEN
        assertThat(orders).hasSize(0);
    }

    @Test
    public void testUsernameWithOrders() {
        // GIVEN
        String username = "carlosz";

        Order[] ordersExpected = from(Order.class).gimme(2, "order 1", "order 2").toArray(new Order[2]);

        // WHEN
        List<Order> orders = orderGateway.findAllByUsername(username);

        // THEN
        assertThat(orders).hasSize(2);
        assertThat(orders)
                .usingElementComparatorIgnoringFields("id", "orderConfirmation", "items")
                .containsExactlyInAnyOrder(ordersExpected);

        List<Purchase> purchasesActual = orders.stream().flatMap(order -> order.getItems().stream()).collect(Collectors.toList());

        Purchase[] purchasesExpected = Arrays.stream(ordersExpected).flatMap(order -> order.getItems().stream()).toArray(Purchase[]::new);

        assertThat(purchasesActual)
                .usingElementComparatorIgnoringFields("id", "product")
                .containsExactlyInAnyOrder(purchasesExpected);

        List<Product> productsActual = purchasesActual.stream().map(Purchase::getProduct).collect(Collectors.toList());

        Product[] productsExpected = Arrays.stream(purchasesExpected).map(Purchase::getProduct).toArray(Product[]::new);

        assertThat(productsActual)
                .usingElementComparatorIgnoringFields("id")
                .containsExactlyInAnyOrder(productsExpected);
    }

    @Test
    public void testRegisterOrder() {
        // GIVEN
        Order order = from(Order.class)
                .uses(new PurchaseAjustProductIdProcessor(productModelCellphoneCaseMotoX4))
                .gimme("order to save");

        // WHEN
        Order orderRegistered = orderGateway.register(order);

        // THEN
        Order orderActual = orderGateway.findById(orderRegistered.getId());
        assertThat(orderActual).isNotNull();
        assertThat(orderActual.getId()).isNotNull();
        assertThat(orderActual.getOrderNumber()).isEqualTo(order.getOrderNumber());
        assertThat(orderActual.getUsername()).isEqualTo(order.getUsername());
        assertThat(orderActual.getValue()).isEqualTo(order.getValue());

        assertThat(orderActual.getItems()).hasSize(1);
    }

    @Test
    public void testFindByOrderId() {
        // GIVEN
        Order order = from(Order.class).gimme("order 1");

        // WHEN
        Order orderActual = orderGateway.findById(orderModelFromProductMotoX4.getId());

        // THEN
        assertThat(orderActual).isNotNull();
        assertThat(orderActual.getId()).isEqualTo(orderModelFromProductMotoX4.getId());
        assertThat(orderActual.getOrderNumber()).isEqualTo(order.getOrderNumber());
        assertThat(orderActual.getUsername()).isEqualTo(order.getUsername());
        assertThat(orderActual.getItems()).hasSize(1);
        assertThat(orderActual.getValue()).isEqualTo(order.getValue());
    }

    @Test
    public void testFindByOrderIdNotExistOrder() {
        // WHEN
        Throwable thrown = catchThrowable(() -> { Order orderActual = orderGateway.findById(3422L); });

        // THEN
        assertThat(thrown)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Order Not Found");
    }
}
