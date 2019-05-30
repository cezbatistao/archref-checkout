package com.cit.checkout.usecase;

import com.cit.checkout.conf.ff4j.Features;
import com.cit.checkout.domain.*;
import com.cit.checkout.domain.exception.FeatureException;
import com.cit.checkout.domain.exception.NotPaidException;
import com.cit.checkout.domain.exception.StockWithoutProductException;
import com.cit.checkout.gateway.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@Component
public class CheckoutOrder {

    private Random generator;

    private GenerateBillet generateBillet;
    private ShoppingCartValidator shoppingCartValidator;
    private PaymentValidator paymentValidator;

    private StockGateway stockGateway;
    private OrderGateway orderGateway;
    private PaymentGateway paymentGateway;
    private ProductGateway productGateway;
    private FeatureGateway featureGateway;

    @Autowired
    public CheckoutOrder(GenerateBillet generateBillet, ShoppingCartValidator shoppingCartValidator, PaymentValidator paymentValidator,
                         StockGateway stockGateway, OrderGateway orderGateway,PaymentGateway paymentGateway,
                         ProductGateway productGateway,FeatureGateway featureGateway) {
        this.generateBillet = generateBillet;
        this.shoppingCartValidator = shoppingCartValidator;
        this.paymentValidator = paymentValidator;

        this.stockGateway = stockGateway;
        this.orderGateway = orderGateway;
        this.paymentGateway = paymentGateway;
        this.productGateway = productGateway;
        this.featureGateway = featureGateway;

        generator = new Random();
    }

    public Order execute(ShoppingCart shoppingCart, Payment payment) {
        shoppingCartValidator.execute(shoppingCart);
        paymentValidator.execute(payment);

        double sumShoppingCart = getSumShoppingCartItens(shoppingCart);

        OrderConfirmation orderConfirmation = null;

        if (featureGateway.isFeatureEnable(Features.BILLET_PAYMENT)) {
            log.info("Feature billet payment active");
            BilletToPay billetToPay = generateBillet.execute(sumShoppingCart);

            orderConfirmation = OrderConfirmationBillet.builder()
                    .billet(billetToPay)
                    .build();
        } else {
            log.info("Feature billet payment deactive");

            if(payment.getType() == PaymentType.BILLET) {
                log.info("Feature billet payment NOT active and payment type is billet");
                throw new FeatureException(Features.BILLET_PAYMENT, "Feature billet payment NOT active");
            }

            PaymentCard paymentCard = (PaymentCard) payment;

            PaymentStatus status = paymentGateway.process(paymentCard, sumShoppingCart);

            if(isPaid(status)) {
                orderConfirmation = OrderConfirmationCard.builder()
                        .transactionId(status.getTransactionId())
                        .build();
            } else {
                throw new NotPaidException("Not paid");
            }
        }

        Order order = generateOrder(shoppingCart, sumShoppingCart, orderConfirmation);

        log.info("Registing order: {}", order);

        orderGateway.register(order);

        return order;
    }

    protected double getSumShoppingCartItens(ShoppingCart shoppingCart) {
        return shoppingCart.getItems()
                    .stream()
                    .map(this::purchase)
                    .mapToDouble(this::getProductPrice)
                    .sum();
    }

    private Purchase purchase(PurchaseItem purchaseItem) {
        Product product = productGateway.findByCode(purchaseItem.getProductCode());

        return Purchase.builder()
                .product(product)
                .quantity(purchaseItem.getQuantity())
                .build();
    }

    private boolean isPaid(PaymentStatus status){
        return Arrays.asList(TransactionStatus.PAID, TransactionStatus.NOT_SENT).contains(status.getStatus());
    }

    private double getProductPrice(Purchase purchase) {
        verifyProductInStock(purchase);
        return purchase.getQuantity() * purchase.getProduct().getValue().doubleValue();
    }

    private void verifyProductInStock(Purchase purchase) {
        Product product = purchase.getProduct();
        Stock stock = stockGateway.getStock(product);

        int totalStock = stock.getTotal();
        int quantityToPurchase = purchase.getQuantity();

        if (totalStock >= quantityToPurchase) {
            stockGateway.lowStock(product, quantityToPurchase);
        } else {
            throw new StockWithoutProductException(product, totalStock,
                    format("Product [%s] without enough in stock, has: %s", product.getCode(), stock.getTotal()));
        }
    }

    private Order generateOrder(ShoppingCart shoppingCart, double sumShoppingCart, OrderConfirmation orderConfirmation) {
        return Order.builder()
                .orderNumber(generateOrderNumber())
                .username(shoppingCart.getUsername())
                .items(shoppingCart.getItems().stream().map(this::purchase).collect(Collectors.toList()))
                .value(BigDecimal.valueOf(sumShoppingCart))
                .orderConfirmation(orderConfirmation)
                .build();
    }

    private Long generateOrderNumber() {
        return Integer.toUnsignedLong(generator.nextInt());
    }
}
