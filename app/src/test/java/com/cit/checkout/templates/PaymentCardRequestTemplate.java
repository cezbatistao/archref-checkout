package com.cit.checkout.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.cit.checkout.gateway.http.json.PaymentCardRequest;

public class PaymentCardRequestTemplate implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(PaymentCardRequest.class)
                .addTemplate(
                        "Credicard without card number",
                        new Rule() {
                            {
                                add("type", "CARD");
                            }
                        })
                .addTemplate(
                        "VISA",
                        new Rule() {
                            {
                                add("type", "CARD");
                                add("cardValue", "4012888888881881");
                            }
                        })
                .addTemplate(
                        "MasterCard",
                        new Rule() {
                            {
                                add("type", "CARD");
                                add("cardValue", "5105105105105100");
                            }
                        });
    }
}
