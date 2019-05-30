package com.cit.checkout.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.cit.checkout.gateway.mysql.model.ProductModel;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductModelTemplate implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(ProductModel.class)
                .addTemplate("model product moto x 4", new Rule() {
                    {
                        add("id", 1L);
                        add("code", "SKU-123-123");
                        add("name", "Moto X 4º");
                        add("value", new BigDecimal("999.99"));
                    }
                });
    }
}
