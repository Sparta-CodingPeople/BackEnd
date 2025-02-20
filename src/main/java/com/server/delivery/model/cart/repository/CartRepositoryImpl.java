package com.server.delivery.model.cart.repository;

import com.server.delivery.model.cart.entity.Cart;
import com.server.delivery.model.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CartRepositoryImpl implements CartRepository {
    private final CartJpaRepository cartJpaRepository;

    @Override
    public Optional<Cart> findByUser(User user) {
        return cartJpaRepository.findByUser(user);
    }

    @Override
    public Cart save(Cart cart) {
        return cartJpaRepository.save(cart);
    }

    @Override
    public void delete(Cart cart) {
        cartJpaRepository.delete(cart);
    }
}
