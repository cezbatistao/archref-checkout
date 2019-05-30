package com.cit.checkout.templates.processor;

import br.com.six2six.fixturefactory.processor.Processor;
import com.cit.checkout.domain.Purchase;
import com.cit.checkout.gateway.mysql.model.ProductModel;

public class PurchaseAjustProductIdProcessor implements Processor {

    private ProductModel productModel;

    public PurchaseAjustProductIdProcessor(ProductModel productModel) {
        this.productModel = productModel;
    }

    @Override
    public void execute(Object fixtureObject) {
        if(fixtureObject instanceof Purchase) {
            Purchase purchase = (Purchase) fixtureObject;
            purchase.getProduct().setId(productModel.getId());
        }
    }
}
