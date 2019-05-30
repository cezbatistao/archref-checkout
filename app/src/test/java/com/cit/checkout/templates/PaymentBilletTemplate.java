package com.cit.checkout.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.cit.checkout.domain.PaymentBillet;
import com.cit.checkout.domain.PaymentType;

public class PaymentBilletTemplate implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(PaymentBillet.class)
                .addTemplate("properties with null values", new Rule() {
                    {
                    }
                })
                .addTemplate("billet", new Rule() {
                    {
                        add("type", PaymentType.BILLET);
                    }
                });
    }
}
