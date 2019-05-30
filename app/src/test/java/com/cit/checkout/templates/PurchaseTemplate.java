package com.cit.checkout.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.cit.checkout.domain.Product;
import com.cit.checkout.domain.Purchase;

public class PurchaseTemplate implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(Purchase.class)
                .addTemplate("purchase product 1", new Rule() {
                    {
                        add("product", one(Product.class, "moto x 4"));
                        add("quantity", 1);
                    }
                })
                .addTemplate("purchase product 2", new Rule() {
                    {
                        add("product", one(Product.class, "cellphone case moto x 4"));
                        add("quantity", 2);
                    }
                });
    }
}
