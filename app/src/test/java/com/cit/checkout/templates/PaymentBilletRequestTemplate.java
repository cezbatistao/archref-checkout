package com.cit.checkout.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.cit.checkout.gateway.http.json.PaymentBilletRequest;

public class PaymentBilletRequestTemplate implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(PaymentBilletRequest.class)
                .addTemplate(
                        "BILLET",
                        new Rule() {
                            {
                                add("type", "BILLET");
                            }
                        });
    }
}
