package com.server.delivery.domain.delivery.dto.res;

import java.time.LocalDateTime;
import java.util.UUID;

import com.server.delivery.model.delivery.entity.Delivery;
import com.server.delivery.model.delivery.entity.DeliveryStatus;
import com.server.delivery.model.order.entity.Order;
import com.server.delivery.model.order.entity.OrderStatus;

public record DeliveryStartResponseDto(
	OrderInfo orderInfo,
	DeliveryInfo deliveryInfo
) {
	public static DeliveryStartResponseDto from(Delivery delivery, Order order) {
		return new DeliveryStartResponseDto(
			OrderInfo.from(order),
			DeliveryInfo.from(delivery)
		);
	}

	public record OrderInfo(
		UUID orderUuid,
		OrderStatus orderStatus,
		String deliveryAddress,
		LocalDateTime orderCreatedAt
	) {
		public static OrderInfo from(Order order) {
			return new OrderInfo(
				order.getOrderUuid(),
				order.getOrderStatus(),
				order.getDeliveryAddress(),
				order.getCreatedAt()
			);
		}
	}

	public record DeliveryInfo(
		UUID deliveryUuid,
		DeliveryStatus deliveryStatus,
		LocalDateTime deliveryStartTime,
		Integer deliveryArrivalTime
	) {
		public static DeliveryInfo from(Delivery delivery) {
			return new DeliveryInfo(
				delivery.getId(),
				delivery.getStatus(),
				delivery.getDeliveryStartTime(),
				delivery.getDeliveryArrivalTime()
			);
		}
	}
}
