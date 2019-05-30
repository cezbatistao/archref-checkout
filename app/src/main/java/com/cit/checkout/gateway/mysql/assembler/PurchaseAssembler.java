package com.cit.checkout.gateway.mysql.assembler;

import com.cit.checkout.domain.Purchase;
import com.cit.checkout.gateway.mysql.model.PurchaseModel;

public class PurchaseAssembler {

    public static Purchase assemble(PurchaseModel purchaseModel) {
        return Purchase.builder()
                .product(ProductAssembler.assemble(purchaseModel.getProduct()))
                .quantity(purchaseModel.getQuantity())
                .build();
    }

    public static PurchaseModel assemble(Purchase purchase) {
        return PurchaseModel.builder()
                .product(ProductAssembler.assemble(purchase.getProduct()))
                .quantity(purchase.getQuantity())
                .build();
    }
}
