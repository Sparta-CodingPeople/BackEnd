package com.server.delivery.domain.cart.service;

import com.server.delivery.domain.cart.dto.request.CreateCartRequestDto;
import com.server.delivery.domain.cart.dto.request.UpdateCartRequestDto;
import com.server.delivery.domain.cart.dto.response.SearchCartResponseDto;

import java.util.UUID;

public interface CartService {
    void createCart(Long userId, CreateCartRequestDto createCartRequestDto);

    SearchCartResponseDto searchCart(Long userId);

    void deleteCart(Long userId);

    void updateCart(Long userId, UpdateCartRequestDto updateCartRequestDto, UUID productId);
}
