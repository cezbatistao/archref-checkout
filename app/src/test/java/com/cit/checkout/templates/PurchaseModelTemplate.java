package com.cit.checkout.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.cit.checkout.gateway.mysql.model.ProductModel;
import com.cit.checkout.gateway.mysql.model.PurchaseModel;

import java.math.BigDecimal;

public class PurchaseModelTemplate implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(PurchaseModel.class)
                .addTemplate("purchase model product 1", new Rule() {
                    {
                        add("id", 1l);
                        add("product", one(ProductModel.class, "model product moto x 4"));
                        add("quantity", 1);
                    }
                })
                .addTemplate("purchase model product 2", new Rule() {
                    {
                        add("id", 2L);
                        add("product", one(ProductModel.class, "model product cellphone case moto x 4"));
                        add("quantity", 2);
                    }
                });
    }
}
