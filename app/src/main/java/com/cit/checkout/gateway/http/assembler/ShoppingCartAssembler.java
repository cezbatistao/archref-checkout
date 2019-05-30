package com.cit.checkout.gateway.http.assembler;

import com.cit.checkout.domain.PurchaseItem;
import com.cit.checkout.domain.ShoppingCart;
import com.cit.checkout.gateway.http.json.ItemRequest;

import java.util.List;
import java.util.stream.Collectors;

public class ShoppingCartAssembler {

    private ShoppingCartAssembler() {
    }

    public static ShoppingCart assemble(String customer, List<ItemRequest> items) {
        return ShoppingCart.builder()
                .username(customer)
                .items(
                        items.stream()
                                .map(ShoppingCartAssembler::createPurchase)
                                .collect(Collectors.toList())
                )
                .build();
    }

    private static PurchaseItem createPurchase(ItemRequest itemRequest) {
        return PurchaseItem.builder()
                .productCode(itemRequest.getProductCode())
                .quantity(itemRequest.getQuantity())
                .build();
    }
}
