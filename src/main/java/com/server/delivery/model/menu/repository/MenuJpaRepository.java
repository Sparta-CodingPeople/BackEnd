package com.server.delivery.model.menu.repository;

import com.server.delivery.model.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuJpaRepository extends JpaRepository<Menu, Long> {
}
