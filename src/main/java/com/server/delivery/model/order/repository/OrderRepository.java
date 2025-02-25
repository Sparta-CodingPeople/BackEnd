package com.server.delivery.model.order.repository;

import com.server.delivery.model.order.entity.Order;
import com.server.delivery.model.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findByOrderUuid(UUID OrderId);

    void delete(Order order);

    Page<Order> findByUserAndStoreNameContaining(User user, Pageable sortedPageable, String keyword);

    Page<Order> findAllByUser(User user, Pageable sortedPageable);

    Page<Order> findByStoreNameContainingAndStoreOwner(User user, Pageable sortedPageable, String keyword);

    Page<Order> findByStoreOwner(User user, Pageable sortedPageable);
}
