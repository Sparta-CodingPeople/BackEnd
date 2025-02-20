package com.server.delivery.model.cart.repository;

import com.server.delivery.model.cart.entity.MenuCart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MenuCartRepositoryImpl implements MenuCartRepository {
    private final MenuCartJpaRepository jpaRepository;

    @Override
    public MenuCart save(MenuCart newMenuCart) {
        return jpaRepository.save(newMenuCart);
    }

    @Override
    public Optional<MenuCart> findByMenuUuid(UUID productId) {
        return jpaRepository.findByMenuCartUuid(productId);
    }

    @Override
    public void delete(MenuCart menuCart) {
        jpaRepository.delete(menuCart);
    }
}
