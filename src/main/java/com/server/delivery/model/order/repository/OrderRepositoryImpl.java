package com.server.delivery.model.order.repository;

import com.server.delivery.model.order.entity.Order;
import com.server.delivery.model.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderJpaRepository jpaRepository;

    @Override
    public Order save(Order order) {
        return jpaRepository.save(order);
    }

    @Override
    public Optional<Order> findByOrderUuid(UUID OrderId) {
        return jpaRepository.findById(OrderId);
    }

    @Override
    public void delete(Order order) {
        jpaRepository.delete(order);
    }

    @Override
    public Page<Order> findByUserAndStoreNameContaining(User user, Pageable sortedPageable, String keyword) {
        return jpaRepository.findByUserAndStoreNameContaining(user, keyword, sortedPageable);
    }


}
