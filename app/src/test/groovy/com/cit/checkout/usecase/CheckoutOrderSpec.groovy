package com.cit.checkout.usecase

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader
import com.cit.checkout.conf.ff4j.Features
import com.cit.checkout.domain.*
import com.cit.checkout.domain.exception.FeatureException
import com.cit.checkout.domain.exception.NotPaidException
import com.cit.checkout.domain.exception.StockWithoutProductException
import com.cit.checkout.gateway.*
import spock.lang.Specification

import static br.com.six2six.fixturefactory.Fixture.from

class CheckoutOrderSpec extends Specification {

    private CheckoutOrder checkoutOrder
    private ShoppingCartValidator shoppingCartValidator
    private PaymentValidator paymentValidator

    private GenerateBillet generateBillet
    private StockGateway stockGateway
    private OrderGateway orderGateway
    private PaymentGateway paymentGateway
    private ProductGateway productGateway
    private FeatureGateway featureGateway

    def setupSpec() {
        FixtureFactoryLoader.loadTemplates("com.cit.checkout.templates")
    }

    def setup() {
        generateBillet = Mock(GenerateBillet)
        shoppingCartValidator = Mock(ShoppingCartValidator)
        paymentValidator = Mock(PaymentValidator)

        stockGateway = Mock(StockGateway)
        orderGateway = Mock(OrderGateway)
        paymentGateway = Mock(PaymentGateway)
        productGateway = Mock(ProductGateway)
        featureGateway = Mock(FeatureGateway)

        checkoutOrder = new CheckoutOrder(generateBillet, shoppingCartValidator, paymentValidator, stockGateway,
                orderGateway, paymentGateway, productGateway, featureGateway)
    }

    def "test verify shopping cart not null"() {
        given: ""
        shoppingCartValidator.execute(null) >> { shoppingCart ->
            throw new IllegalArgumentException('Shopping cart cannot be null')
        }

        when: ""
        def order = checkoutOrder.execute(null, null)

        then: ""
        order == null

        and:
        IllegalArgumentException ex = thrown()
        ex.message == 'Shopping cart cannot be null'
    }

    def "test verify payment method not null"() {
        given: ""
        paymentValidator.execute(null) >> { payment ->
            throw new IllegalArgumentException('Payment cannot be null')
        }

        ShoppingCart shoppingCart = from(ShoppingCart.class).gimme("shopping cart with one item")

        when: ""
        def order = checkoutOrder.execute(shoppingCart, null)

        then: ""
        order == null

        and:
        IllegalArgumentException ex = thrown()
        ex.message == 'Payment cannot be null'
    }

    def "test checkout generate order shopping cart with one product but not approved credit card"() {
        given: ""
        ShoppingCart shoppingCart = from(ShoppingCart.class).gimme("shopping cart with one item")

        Product product = from(Product.class).gimme("moto x 4")
        productGateway.findByCode(product.code) >> product

        Stock stock = from(Stock.class).gimme("product 1 with one in stock")
        stockGateway.getStock(product) >> stock

        featureGateway.isFeatureEnable(Features.BILLET_PAYMENT) >> false

        orderGateway.register(_) >> _

        Payment creditCardPayment = from(PaymentCard.class).gimme("mastercard")
        paymentGateway.process(_ as Payment, _ as Double) >> { payment, value ->
            throw new NotPaidException('Error processing credit card payment')
        }

        when: ""
        def order = checkoutOrder.execute(shoppingCart, creditCardPayment)

        then: ""
        order == null

        and:
        NotPaidException ex = thrown()
        ex.message == 'Error processing credit card payment'
    }

    def "test checkout generate order shopping cart with one product"() {
        given: ""
        ShoppingCart shoppingCart = from(ShoppingCart.class).gimme("shopping cart with one item")

        Product product = from(Product.class).gimme("moto x 4")
        productGateway.findByCode(product.code) >> product

        Payment creditCardPayment = from(PaymentCard.class).gimme("mastercard")

        Stock stock = from(Stock.class).gimme("product 1 with one in stock")
        stockGateway.getStock(_) >> stock

        featureGateway.isFeatureEnable(Features.BILLET_PAYMENT) >> false

        orderGateway.register(_) >> _

        paymentGateway.process(_, _) >> from(PaymentStatus.class).gimme("PAID")

        when: ""
        def order = checkoutOrder.execute(shoppingCart, creditCardPayment)

        then: ""
//        order.id != null // TODO
        order.orderNumber != null
        order.value == new BigDecimal("999.99")
        order.username == shoppingCart.getUsername()
        order.items.size() == 1
        order.orderConfirmation.transactionId != ""

        and: ""
        order.items[0].quantity == 1
        order.items[0].product.code == product.code
    }

    def "test checkout do not generate order for unaproved payment"() {
        given: ""
        ShoppingCart shoppingCart = from(ShoppingCart.class).gimme("shopping cart with one item")

        Product product = from(Product.class).gimme("moto x 4")
        productGateway.findByCode(product.code) >> product

        Payment creditCardPayment = from(PaymentCard.class).gimme("mastercard")

        Stock stock = from(Stock.class).gimme("product 1 with one in stock")
        stockGateway.getStock(_) >> stock

        featureGateway.isFeatureEnable(Features.BILLET_PAYMENT) >> false

        orderGateway.register(_) >> _

        paymentGateway.process(_, _) >> from(PaymentStatus.class).gimme("INSUFFICIENT_FUNDS")

        when: ""
        def order = checkoutOrder.execute(shoppingCart, creditCardPayment)

        then: ""
        NotPaidException ex = thrown()
    }

    def "test checkout without product in stock"() {
        given: ""
        ShoppingCart shoppingCart = from(ShoppingCart.class).gimme("shopping cart with two items")

        featureGateway.isFeatureEnable(Features.BILLET_PAYMENT) >> false

        Product product1 = from(Product.class).gimme("moto x 4")
        productGateway.findByCode(product1.code) >> product1

        Product product2 = from(Product.class).gimme("cellphone case moto x 4")
        productGateway.findByCode(product2.code) >> product2

        Stock product1Stock = from(Stock.class).gimme("product 1 with one in stock")
        stockGateway.getStock(product1) >> product1Stock

        Stock product2Stock = from(Stock.class).gimme("product 2 with one in stock")
        stockGateway.getStock(product2) >> product2Stock

        Payment creditCardPayment = from(PaymentCard.class).gimme("mastercard")

        when: ""
        def order = checkoutOrder.execute(shoppingCart, creditCardPayment)

        then: ""
        order == null

        and:
        StockWithoutProductException ex = thrown()
        ex.message == 'Product [SKU-321-321] without enough in stock, has: 1'
        ex.product == product2
        ex.quantityInStock == 1
    }

    def "test payment with billet feature active"() {
        given: ""
        ShoppingCart shoppingCart = from(ShoppingCart.class).gimme("shopping cart with one item")
        Payment billetPayment = from(PaymentBillet.class).gimme("billet")

        Product product = from(Product.class).gimme("moto x 4")
        productGateway.findByCode(product.code) >> product

        Stock stock = from(Stock.class).gimme("product 1 with one in stock")
        stockGateway.getStock(_) >> stock

        orderGateway.register(_) >> _

        featureGateway.isFeatureEnable(Features.BILLET_PAYMENT) >> true

        BilletToPay billetToPay = from(BilletToPay.class).gimme("billet to due in 7 days")
        generateBillet.execute(billetToPay.value.toDouble()) >> billetToPay

        when: ""
        def order = checkoutOrder.execute(shoppingCart, billetPayment)

        then: ""
//        order.id != null // TODO
        order.orderNumber != null
        order.value == new BigDecimal("999.99")
        order.username == shoppingCart.getUsername()
        order.items.size() == 1

        and: ""
        order.items[0].quantity == 1
        order.items[0].product.code == product.code

        and: "" // TODO
//        order.or.value != null
//        order.billet.number != null
//        order.billet.dueDate != null
    }

    def "test payment with billet feature NOT active"() {
        given: ""
        ShoppingCart shoppingCart = from(ShoppingCart.class).gimme("shopping cart with one item")
        Payment billetPayment = from(PaymentBillet.class).gimme("billet")

        Product product = from(Product.class).gimme("moto x 4")
        productGateway.findByCode(product.code) >> product

        Stock stock = from(Stock.class).gimme("product 1 with one in stock")
        stockGateway.getStock(_) >> stock

        orderGateway.register(_) >> _

        featureGateway.isFeatureEnable(Features.BILLET_PAYMENT) >> false

        BilletToPay billetToPay = from(BilletToPay.class).gimme("billet to due in 7 days")
        generateBillet.execute(billetToPay.value.toDouble()) >> billetToPay

        when: ""
        def order = checkoutOrder.execute(shoppingCart, billetPayment)

        then: ""
        FeatureException ex = thrown()
        ex.featureFlag == Features.BILLET_PAYMENT
        ex.message == 'Feature billet payment NOT active'
    }

    def "test sum shopping card to checkout"() {
        given: ""
        ShoppingCart shoppingCart = from(ShoppingCart.class).gimme("shopping cart with two items")

        featureGateway.isFeatureEnable(Features.BILLET_PAYMENT) >> false

        Product product1 = from(Product.class).gimme("moto x 4")
        productGateway.findByCode(product1.code) >> product1

        Product product2 = from(Product.class).gimme("cellphone case moto x 4")
        productGateway.findByCode(product2.code) >> product2

        Stock product1Stock = from(Stock.class).gimme("product 1 with one in stock")
        stockGateway.getStock(product1) >> product1Stock

        Stock product2Stock = from(Stock.class).gimme("product 2 with two in stock")
        stockGateway.getStock(product2) >> product2Stock

        when: ""
        def sumShoppingCart = checkoutOrder.getSumShoppingCartItens(shoppingCart)

        then: ""
        sumShoppingCart == 1021.79
    }
}
