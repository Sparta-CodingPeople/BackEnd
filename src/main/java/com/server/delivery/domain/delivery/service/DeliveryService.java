package com.server.delivery.domain.delivery.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.server.delivery.common.exception.ExceptionCode;
import com.server.delivery.common.exception.customException.CustomOrderException;
import com.server.delivery.domain.delivery.dto.res.DeliverySearchResponseDto;
import com.server.delivery.model.delivery.entity.Delivery;
import com.server.delivery.model.order.entity.Order;
import com.server.delivery.model.order.entity.OrderStatus;
import com.server.delivery.model.order.repository.OrderJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryService {
	private final OrderJpaRepository orderJpaRepository;

	public DeliverySearchResponseDto searchDeliveryInfo(UUID orderId) {
		// 주문 조회
		Order foundOrder = orderJpaRepository.findByUuid(orderId)
			.orElseThrow(() -> new CustomOrderException(ExceptionCode.ORDER_NOT_FOUND));

		Delivery foundDelivery = foundOrder.getDelivery();

		// 주문 상태 확인
		// 주문 상태가 ACCEPT이면 deliveryStartTime update
		if (foundOrder.getOrderStatus() == OrderStatus.ACCEPT) {
			foundDelivery.updateDeliveryStartTime(foundOrder);
		}

		foundDelivery.updateDeliveryEstimatedTime(foundOrder);

		return DeliverySearchResponseDto.of(foundOrder, foundDelivery);
	}
}
