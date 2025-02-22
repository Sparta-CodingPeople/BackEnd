package com.server.delivery.domain.cart.dto;

import com.server.delivery.model.cart.entity.MenuCart;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class CartItemDto {
    private UUID productId;
    private String productName;
    private int quantity;
    private int totalPrice;

    public static CartItemDto from(MenuCart menuCart) {
        return CartItemDto.builder()
                .productId(menuCart.getMenuCartUuid())
                .productName(menuCart.getMenu().getMenuName())
                .quantity(menuCart.getQuantity())
                .totalPrice(menuCart.getTotalPrice())
                .build();
    }
}
