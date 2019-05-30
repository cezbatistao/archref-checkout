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
                        "without product and negative quantity",
                        new Rule() {
                            {
                                add("quantity", -1);
                            }
                        })
                .addTemplate(
                        "1 product for 200",
                        new Rule() {
                            {
                                add("productCode", "SKU-123-432");
                                add("quantity", 1);
                            }
                        })
                .addTemplate(
                        "3 product for 27.90",
                        new Rule() {
                            {
                                add("productCode", "SKU-322-222");
                                add("quantity", 3);
                            }
                        })
                .addTemplate(
                        "5 product for 132",
                        new Rule() {
                            {
                                add("productCode", "SKU-341-568");
                                add("quantity", 5);
                            }
                        });
    }
}
