package com.cit.checkout.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.cit.checkout.domain.Product;
import com.cit.checkout.domain.Stock;

import java.util.UUID;

public class StockTemplate implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(Stock.class)
                .addTemplate("product 1 with one in stock", new Rule() {
                    {
                        add("id", 1L);
                        add("product", one(Product.class, "moto x 4"));
                        add("total", 1);
                    }
                })
                .addTemplate("product 2 with one in stock", new Rule() {
                    {
                        add("id", 2L);
                        add("product", one(Product.class, "cellphone case moto x 4"));
                        add("total", 1);
                    }
                })
                .addTemplate("product 2 with two in stock", new Rule() {
                    {
                        add("id", 2L);
                        add("product", one(Product.class, "cellphone case moto x 4"));
                        add("total", 2);
                    }
                });
    }
}
