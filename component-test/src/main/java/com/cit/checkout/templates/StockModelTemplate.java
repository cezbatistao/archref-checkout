package com.cit.checkout.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.cit.checkout.gateway.mysql.model.ProductModel;
import com.cit.checkout.gateway.mysql.model.StockModel;

public class StockModelTemplate implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(StockModel.class)
                .addTemplate("model product 1 with one in stock", new Rule() {
                    {
                        add("id", 1L);
                        add("product", one(ProductModel.class,"model product moto x 4"));
                        add("total", 1);
                    }
                });
    }
}
