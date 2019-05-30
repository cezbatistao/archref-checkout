package com.cit.checkout.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.cit.checkout.domain.PurchaseItem;

public class PurchaseItemTemplate implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(PurchaseItem.class)
                .addTemplate("purchase item with null properties", new Rule() {
                    {
                    }
                })
                .addTemplate("purchase item with product code empty and negative quantity", new Rule() {
                    {
                        add("productCode", "");
                        add("quantity", -1);
                    }
                })
                .addTemplate("purchase item product 1", new Rule() {
                    {
                        add("productCode", "SKU-123-123");
                        add("quantity", 1);
                    }
                })
                .addTemplate("purchase item product 2", new Rule() {
                    {
                        add("productCode", "SKU-321-321");
                        add("quantity", 2);
                    }
                });
    }
}
