package com.server.delivery.domain.delivery.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.server.delivery.common.exception.ExceptionCode;
import com.server.delivery.common.exception.customException.CustomDeliveryException;
import com.server.delivery.common.exception.customException.CustomOrderException;
import com.server.delivery.domain.delivery.dto.req.DeliveryCompleteRequestDto;
import com.server.delivery.domain.delivery.dto.req.DeliveryStartRequestDto;
import com.server.delivery.domain.delivery.dto.res.DeliveryCompleteResponseDto;
import com.server.delivery.domain.delivery.dto.res.DeliveryStartResponseDto;
import com.server.delivery.domain.delivery.repository.DeliveryJpaRepository;
import com.server.delivery.model.delivery.entity.Delivery;
import com.server.delivery.model.delivery.entity.DeliveryStatus;
import com.server.delivery.model.order.entity.Order;
import com.server.delivery.model.order.repository.OrderJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryService {
	private final OrderJpaRepository orderJpaRepository;
	private final DeliveryJpaRepository deliveryJpaRepository;

	@Transactional
	public DeliveryStartResponseDto startDelivery(UUID deliveryId, DeliveryStartRequestDto request) {
		Delivery foundDelivery = deliveryJpaRepository.findByDeliveryUuid(deliveryId)
			.orElseThrow(() -> new CustomDeliveryException(ExceptionCode.DELIVERY_NOT_FOUND));

		if (DeliveryStatus.isAlreadyStarted(foundDelivery.getStatus())) {
			throw new CustomDeliveryException(ExceptionCode.DELIVERY_ALREADY_START);
		}

		foundDelivery.changeStatusToDelivering();

		Order foundOrder = orderJpaRepository.findByOrderUuid(request.orderUuid())
			.orElseThrow(() -> new CustomOrderException(ExceptionCode.ORDER_NOT_FOUND));

		validateOrderDelivery(foundDelivery, foundOrder);

		return DeliveryStartResponseDto.from(foundDelivery, foundOrder);
	}

	@Transactional
	public DeliveryCompleteResponseDto completeDelivery(UUID deliveryId, DeliveryCompleteRequestDto request) {
		Delivery foundDelivery = deliveryJpaRepository.findByDeliveryUuid(deliveryId)
			.orElseThrow(() -> new CustomDeliveryException(ExceptionCode.DELIVERY_NOT_FOUND));

		if (DeliveryStatus.isNotDelivering(foundDelivery.getStatus())) {
			throw new CustomDeliveryException(ExceptionCode.DELIVERY_ALREADY_COMPLETED);
		}

		if (DeliveryStatus.isCompletedDelivery(foundDelivery.getStatus())) {
			throw new CustomDeliveryException(ExceptionCode.DELIVERY_ALREADY_DELIVERED);
		}

		foundDelivery.changeStatusToCompleted();

		Order foundOrder = orderJpaRepository.findByOrderUuid(request.orderUuid())
			.orElseThrow(() -> new CustomOrderException(ExceptionCode.ORDER_NOT_FOUND));

		validateOrderDelivery(foundDelivery, foundOrder);

		return DeliveryCompleteResponseDto.from(foundDelivery, foundOrder);
	}

	private void validateOrderDelivery(Delivery delivery, Order order) {
		if (!delivery.getOrder().getOrderUuid().equals(order.getOrderUuid())) {
			throw new CustomOrderException(ExceptionCode.ORDER_NOT_FOUND);
		}
	}
}
