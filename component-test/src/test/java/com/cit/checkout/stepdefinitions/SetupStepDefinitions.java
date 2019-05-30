package com.cit.checkout.stepdefinitions;

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.cit.checkout.ApplicationConfiguration;
import com.cit.checkout.config.ff4j.Features;
import com.cit.checkout.gateway.mysql.OrderRepository;
import com.cit.checkout.gateway.mysql.ProductRepository;
import com.cit.checkout.gateway.mysql.StockRepository;
import cucumber.api.java.Before;
import org.ff4j.FF4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest(classes = ApplicationConfiguration.class)
@WebAppConfiguration
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class SetupStepDefinitions {

    private StockRepository stockRepository;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;

    private FF4j ff4j;

    @Autowired
    public SetupStepDefinitions(
            StockRepository stockRepository,
            ProductRepository productRepository,
            OrderRepository orderRepository,
            FF4j ff4j) {
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.ff4j = ff4j;
    }

    @Before("@FixtureLoad")
    public void initializeFixtureTemplate() throws Throwable {
        FixtureFactoryLoader.loadTemplates("com.cit.checkout.templates");
    }

    @Before("@CleanDatabase")
    public void cleanupDatabase() throws Throwable {
        stockRepository.deleteAll();
        orderRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Before("@EnableFeatures")
    public void enableFeatures() throws Throwable {
        for (Features feature : Features.values()) {
            ff4j.enable(feature.getKey());
        }
    }
}
