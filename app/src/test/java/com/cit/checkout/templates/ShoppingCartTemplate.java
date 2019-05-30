package com.cit.checkout.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.cit.checkout.domain.Purchase;
import com.cit.checkout.domain.PurchaseItem;
import com.cit.checkout.domain.ShoppingCart;

public class ShoppingCartTemplate implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(ShoppingCart.class)
                .addTemplate("shopping cart with username null", new Rule() {
                    {
                    }
                })
                .addTemplate("shopping cart with username empty", new Rule() {
                    {
                        add("username", "");
                        add("items", has(1).of(PurchaseItem.class, "purchase item product 1"));
                    }
                })
                .addTemplate("shopping cart with username blank", new Rule() {
                    {
                        add("username", "  ");
                        add("items", has(1).of(PurchaseItem.class, "purchase item product 1"));
                    }
                })
                .addTemplate("shopping cart with items null properties", new Rule() {
                    {
                        add("username", "carlosz");
                        add("items", has(1).of(PurchaseItem.class, "purchase item with null properties"));
                    }
                })
                .addTemplate("shopping cart with items wrong properties", new Rule() {
                    {
                        add("username", "carlosz");
                        add("items", has(1).of(PurchaseItem.class, "purchase item with product code empty and negative quantity"));
                    }
                })
                .addTemplate("shopping cart with items have product code blank and zero quantity", new Rule() {
                    {
                        add("username", "carlosz");
                        add("items", has(1).of(PurchaseItem.class, "purchase item with product code empty and negative quantity"));
                    }
                })
                .addTemplate("shopping cart with one item", new Rule() {
                    {
                        add("username", "carlosz");
                        add("items", has(1).of(PurchaseItem.class, "purchase item product 1"));
                    }
                })
                .addTemplate("shopping cart with no item", new Rule() {
                    {
                        add("username", "carlosz");
                        add("items", has(0).of(PurchaseItem.class, "purchase item product 1"));
                    }
                })
                .addTemplate("shopping cart with null item", new Rule() {
                    {
                        add("username", "carlosz");
                    }
                })
                .addTemplate("shopping cart with two items", new Rule() {
                    {
                        add("username", "carlosz");
                        add("items", has(2).of(PurchaseItem.class,
                                "purchase item product 1", "purchase item product 2"));
                    }
                });
    }
}
