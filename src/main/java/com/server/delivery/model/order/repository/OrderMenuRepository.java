package com.server.delivery.model.order.repository;

import com.server.delivery.model.order.entity.OrderMenu;

import java.util.Optional;
import java.util.UUID;

public interface OrderMenuRepository {

    OrderMenu save(OrderMenu orderMenu);

    Optional<OrderMenu> findById(UUID menuId);

    void delete(OrderMenu orderMenu);
}
