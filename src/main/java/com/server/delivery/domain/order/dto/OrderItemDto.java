package com.server.delivery.domain.order.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class OrderItemDto {
    private UUID productId;
    private String productName;
    private int productCount;
    private int productPrice;
}
