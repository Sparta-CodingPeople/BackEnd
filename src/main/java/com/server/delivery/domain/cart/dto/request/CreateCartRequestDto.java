package com.server.delivery.domain.cart.dto.request;

import com.server.delivery.model.cart.entity.Cart;
import com.server.delivery.model.cart.entity.MenuCart;
import com.server.delivery.model.menu.entity.Menu;
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

    public MenuCart to(Menu menu, Cart cart, int quantity) {
        return MenuCart.builder()
                .menu(menu)  // dto에서 받은 메뉴 객체
                .cart(cart)  // 기존 장바구니
                .quantity(quantity)  // 요청된 수량
                .totalPrice(quantity * menu.getMenuPrice())  // 요청된 가격
                .build();
    }
}
