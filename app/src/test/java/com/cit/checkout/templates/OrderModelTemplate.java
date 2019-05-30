package com.cit.checkout.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.cit.checkout.gateway.mysql.model.OrderModel;
import com.cit.checkout.gateway.mysql.model.PurchaseModel;

import java.math.BigDecimal;

public class OrderModelTemplate implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(OrderModel.class)
                .addTemplate("order model 1", new Rule() {
                    {
                        add("id", 1L);
                        add("orderNumber", 5435435L);
                        add("username", "carlosz");
                        add("items", has(1).of(PurchaseModel.class, "purchase model product 1"));
                        add("value", new BigDecimal("999.99"));
                    }
                })
                .addTemplate("order model 2", new Rule() {
                    {
                        add("id", 2L);
                        add("orderNumber", 23423532L);
                        add("username", "carlosz");
                        add("items", has(1).of(PurchaseModel.class, "purchase model product 2"));
                        add("value", new BigDecimal("21.80"));
                    }
                });
    }
}
