package com.server.delivery.model.cart.repository;

import com.server.delivery.model.cart.entity.MenuCart;

import java.util.Optional;
import java.util.UUID;

public interface MenuCartRepository {
    MenuCart save(MenuCart newMenuCart);

    Optional<MenuCart> findByMenuUuid(UUID productId);

    void delete(MenuCart menuCart);
}
