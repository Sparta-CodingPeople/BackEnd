package com.server.delivery.model.cart.repository;

import com.server.delivery.model.cart.entity.MenuCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MenuCartJpaRepository extends JpaRepository<MenuCart, UUID> {
    Optional<MenuCart> findByMenuCartUuid(UUID productId);
}
