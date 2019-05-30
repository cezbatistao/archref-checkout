package com.cit.checkout.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.cit.checkout.gateway.mysql.model.ProductModel;

import java.math.BigDecimal;

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
                })
                .addTemplate("model product cellphone case moto x 4", new Rule() {
                    {
                        add("id", 2L);
                        add("code", "SKU-321-321");
                        add("name", "Capa Moto X4");
                        add("value", new BigDecimal("10.90"));
                    }
                })
                .addTemplate("model product without stock", new Rule() {
                    {
                        add("id", 3L);
                        add("code", "SKU-456-456");
                        add("name", "Película Moto X4");
                        add("value", new BigDecimal("50.00"));
                    }
                });
    }
}
