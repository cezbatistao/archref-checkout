package com.cit.checkout.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.cit.checkout.gateway.http.json.ItemRequest;

public class ItemRequestTemplate  implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(ItemRequest.class)
                .addTemplate(
                        "One Moto X4",
                        new Rule() {
                            {
                                add("productCode", "SKU-123-123");
                                add("quantity", 1);
                            }
                        });
    }
}
