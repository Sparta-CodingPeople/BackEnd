package com.server.delivery.model.order.repository;

import com.server.delivery.model.order.entity.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findById(UUID OrderId);
    void delete(Order order);
}
