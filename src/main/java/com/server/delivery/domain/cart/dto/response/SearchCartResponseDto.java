package com.server.delivery.domain.cart.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class SearchCartResponseDto {
    private UUID cartId;
    private int totalPrice;

}
