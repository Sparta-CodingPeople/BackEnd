package com.server.delivery.domain.cart.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class CreateCartRequestDto {
    private UUID productId;
    private int quantity;
}
