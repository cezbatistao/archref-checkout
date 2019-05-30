package com.cit.checkout.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.cit.checkout.domain.Order;
import com.cit.checkout.domain.OrderConfirmationBillet;
import com.cit.checkout.domain.OrderConfirmationCard;
import com.cit.checkout.domain.Purchase;

import java.math.BigDecimal;

public class OrderTemplate implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(Order.class)
                .addTemplate("order 1", new Rule() {
                    {
                        add("orderNumber", 5435435L);
                        add("username", "carlosz");
                        add("items", has(1).of(Purchase.class, "purchase product 1"));
                        add("value", new BigDecimal("999.99"));
                        add("orderConfirmation", one(OrderConfirmationCard.class, "order confirmation to credit card"));
                    }
                })
                .addTemplate("order 2", new Rule() {
                    {
                        add("orderNumber", 23423532L);
                        add("username", "carlosz");
                        add("items", has(1).of(Purchase.class, "purchase product 2"));
                        add("value", new BigDecimal("21.80"));
                        add("orderConfirmation", one(OrderConfirmationCard.class, "order confirmation to credit card"));
                    }
                })
                .addTemplate("order with billet payment", new Rule() {
                    {
                        add("orderNumber", 78945L);
                        add("username", "eduardoz");
                        add("items", has(1).of(Purchase.class, "purchase product 1"));
                        add("value", new BigDecimal("999.99"));
                        add("orderConfirmation", one(OrderConfirmationBillet.class, "order confirmation to billet"));
                    }
                })
                .addTemplate("order to save", new Rule() {
                    {
                        add("orderNumber", 7878776L);
                        add("username", "carlosz");
                        add("items", has(1).of(Purchase.class, "purchase product 2"));
                        add("value", new BigDecimal("21.80"));
                    }
                });
    }
}
