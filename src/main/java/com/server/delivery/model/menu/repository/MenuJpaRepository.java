package com.server.delivery.model.menu.repository;

import com.server.delivery.model.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MenuJpaRepository extends JpaRepository<Menu, UUID> {
    Optional<Menu> findByMenuUuId(UUID productId);
}
