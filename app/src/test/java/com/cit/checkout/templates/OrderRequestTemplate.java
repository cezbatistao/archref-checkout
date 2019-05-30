package com.cit.checkout.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.cit.checkout.gateway.http.json.*;

public class OrderRequestTemplate implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(OrderRequest.class)
                .addTemplate("Invalid order with Credicard",
                        new Rule() {
                            {
                                add("items", has(1).of(ItemRequest.class, "without product and negative quantity"));
                                add("payment", one(PaymentCardRequest.class, "Credicard without card number"));
                            }
                        })
                .addTemplate("Valid order with VISA",
                        new Rule() {
                            {
                                add("items", has(1).of(ItemRequest.class, "1 product for 200"));
                                add("payment", one(PaymentCardRequest.class, "VISA"));
                            }
                        })
                .addTemplate("Invalid order with VISA",
                        new Rule() {
                            {
                                add("items", has(0).of(ItemRequest.class));
                                add("payment", one(PaymentCardRequest.class, "VISA"));
                            }
                        })
                .addTemplate("Invalid order with no payment",
                        new Rule() {
                            {
                                add("items", has(0).of(ItemRequest.class));
                            }
                        });
    }
}
