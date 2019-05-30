package com.cit.checkout.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.cit.checkout.domain.Product;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductTemplate implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(Product.class)
                .addTemplate("moto x 4", new Rule() {
                    {
                        add("id", 1L);
                        add("code", "SKU-123-123");
                        add("name", "Moto X 4º");
                        add("value", new BigDecimal("999.99"));
                    }
                })
                .addTemplate("cellphone case moto x 4", new Rule() {
                    {
                        add("id", 2L);
                        add("code", "SKU-321-321");
                        add("name", "Capa Moto X4");
                        add("value", new BigDecimal("10.90"));
                    }
                })
                .addTemplate("product don't exists", new Rule() {
                    {
                        add("id", -1L);
                        add("code", "SKU-1-1");
                        add("name", "Produto inexistente");
                        add("value", new BigDecimal("1.99"));
                    }
                });
    }
}