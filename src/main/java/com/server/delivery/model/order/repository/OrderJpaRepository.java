package com.server.delivery.model.order.repository;

import com.server.delivery.model.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderJpaRepository extends JpaRepository<Order, UUID> {
    void findByOrderId(UUID orderId);
}
