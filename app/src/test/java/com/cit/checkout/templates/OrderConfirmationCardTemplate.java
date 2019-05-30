package com.cit.checkout.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.cit.checkout.domain.Order;
import com.cit.checkout.domain.OrderConfirmationCard;
import com.cit.checkout.domain.Purchase;

import java.math.BigDecimal;

public class OrderConfirmationCardTemplate implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(OrderConfirmationCard.class)
                .addTemplate("order confirmation to credit card", new Rule() {
                    {
                        add("transactionId", "454564235245");
                    }
                });
    }
}
