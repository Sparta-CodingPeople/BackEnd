package com.server.delivery.util.helper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.server.delivery.common.exception.ExceptionCode;
import com.server.delivery.common.exception.customException.CustomDeliveryException;
import com.server.delivery.domain.delivery.repository.DeliveryJpaRepository;
import com.server.delivery.model.delivery.entity.Delivery;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeliveryHelper {
	private final DeliveryJpaRepository deliveryJpaRepository;

	public Delivery getDelivery(UUID deliveryId) {
		return deliveryJpaRepository.findById(deliveryId)
			.orElseThrow(() -> new CustomDeliveryException(ExceptionCode.DELIVERY_NOT_FOUND));
	}

	public Delivery save(Delivery delivery) {
		return deliveryJpaRepository.save(delivery);
	}
}
