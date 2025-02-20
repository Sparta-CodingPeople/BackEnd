package com.server.delivery.model.order.repository;

import com.server.delivery.model.order.entity.OrderMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderMenuJpaRepository extends JpaRepository<OrderMenu, UUID> {
}
