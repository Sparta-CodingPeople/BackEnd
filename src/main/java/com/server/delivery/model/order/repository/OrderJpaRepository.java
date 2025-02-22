package com.server.delivery.model.order.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.server.delivery.model.order.entity.Order;

@Repository
public interface OrderJpaRepository extends JpaRepository<Order, UUID> {

	@Query("select o from Order o where o.id = :orderId")
	Optional<Order> findByUuid(UUID orderId);
}
