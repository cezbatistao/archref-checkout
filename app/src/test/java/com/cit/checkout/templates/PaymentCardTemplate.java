package com.cit.checkout.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.cit.checkout.domain.Payment;
import com.cit.checkout.domain.PaymentCard;
import com.cit.checkout.domain.PaymentType;

public class PaymentCardTemplate implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(PaymentCard.class)
                .addTemplate("properties with null", new Rule() {
                    {
                    }
                })
                .addTemplate("credicard with blank value", new Rule() {
                    {
                        add("type", PaymentType.CARD);
                        add("creditCard", " ");
                    }
                })
                .addTemplate("mastercard", new Rule() {
                    {
                        add("type", PaymentType.CARD);
                        add("creditCard", "1234567898765432");
                    }
                });
    }
}
