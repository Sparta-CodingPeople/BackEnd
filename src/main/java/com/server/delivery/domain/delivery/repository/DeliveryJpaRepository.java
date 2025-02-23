package com.server.delivery.domain.delivery.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.server.delivery.model.delivery.entity.Delivery;

public interface DeliveryJpaRepository extends JpaRepository<Delivery, UUID> {
}
