package com.server.delivery.domain.cart.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateCartRequestDto {
    private int quantity;
}
