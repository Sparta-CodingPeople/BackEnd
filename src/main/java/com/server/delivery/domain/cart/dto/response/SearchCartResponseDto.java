package com.server.delivery.domain.cart.dto.response;

import com.server.delivery.domain.cart.dto.CartItemDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class SearchCartResponseDto {
    private UUID cartId;
    private List<CartItemDto> items;
    private int totalPrice;
    private int totalQuantity;

    public static SearchCartResponseDto from(int totalPrice, int totalQuantity, List<CartItemDto> items, UUID cartUuid) {
        return SearchCartResponseDto.builder()
                .cartId(cartUuid)
                .items(items)
                .totalPrice(totalPrice)
                .totalQuantity(totalQuantity)
                .build();


    }

}
