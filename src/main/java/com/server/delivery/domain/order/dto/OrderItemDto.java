package com.server.delivery.domain.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {
    private String productId;
    private int productCount;
    private String productName;
    private int productPrice;
}
