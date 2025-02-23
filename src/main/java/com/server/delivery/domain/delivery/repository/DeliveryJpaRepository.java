package com.server.delivery.domain.delivery.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.server.delivery.model.delivery.entity.Delivery;

public interface DeliveryJpaRepository extends JpaRepository<Delivery, UUID> {
	@Query("select d from Delivery d where d.id = :deliveryId")
	Optional<Delivery> findByDeliveryUuid(UUID deliveryId);
}
