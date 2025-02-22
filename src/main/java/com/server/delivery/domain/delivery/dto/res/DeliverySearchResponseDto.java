package com.server.delivery.domain.delivery.dto.res;

import java.time.LocalDateTime;
import java.util.UUID;

import com.server.delivery.model.delivery.entity.Delivery;
import com.server.delivery.model.delivery.entity.DeliveryStatus;
import com.server.delivery.model.order.entity.Order;

public record DeliverySearchResponseDto(
	UUID orderId,
	LocalDateTime orderCreatedAt,
	UUID deliveryId,
	String deliveryAddress,
	DeliveryStatus deliveryStatus,
	LocalDateTime deliveryStartTime,
	int estimatedDeliveryTime
) {
	public static DeliverySearchResponseDto of(Order order, Delivery delivery) {
		return new DeliverySearchResponseDto(
			order.getId(),
			order.getCreatedAt(),
			delivery.getId(),
			order.getDeliveryAddress(),
			delivery.getStatus(),
			delivery.getDeliveryStartTime(),
			delivery.getDeliveryArrivalTime()
		);
	}
}
