package com.server.delivery.model.cart.repository;

import com.server.delivery.model.cart.entity.Cart;
import com.server.delivery.model.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartJpaRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findByUser(User user);
}
