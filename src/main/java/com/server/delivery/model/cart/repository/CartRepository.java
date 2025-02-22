package com.server.delivery.model.cart.repository;

import com.server.delivery.model.cart.entity.Cart;
import com.server.delivery.model.user.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository {
    Optional<Cart> findByUser(User user);

    Cart save(Cart cart);

    void delete(Cart cart);

    Optional<Cart> findByCartUuid(UUID cartUuid);
}
