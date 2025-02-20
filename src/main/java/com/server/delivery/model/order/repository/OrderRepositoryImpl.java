package com.server.delivery.model.order.repository;

import com.server.delivery.model.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderJpaRepository jpaRepository;

    @Override
    public Order save(Order order) {return jpaRepository.save(order);}

    @Override
    public Optional<Order> findById(UUID OrderId) {return jpaRepository.findById(OrderId);    }

    @Override
    public void delete(Order order){
        jpaRepository.delete(order);
    }



}
