package com.server.delivery.domain.cart.dto;

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
    private int price;
}
