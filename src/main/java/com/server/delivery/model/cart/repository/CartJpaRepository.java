package com.server.delivery.model.cart.repository;

import com.server.delivery.model.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartJpaRepository extends JpaRepository<Cart, String> {
}
