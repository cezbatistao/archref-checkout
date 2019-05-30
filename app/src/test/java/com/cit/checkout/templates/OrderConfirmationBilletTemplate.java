package com.cit.checkout.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.cit.checkout.domain.BilletToPay;
import com.cit.checkout.domain.OrderConfirmationBillet;

public class OrderConfirmationBilletTemplate implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(OrderConfirmationBillet.class)
                .addTemplate("order confirmation to billet", new Rule() {
                    {
                        add("billet", one(BilletToPay.class, "billet to due in 7 days"));
                    }
                });
    }
}
