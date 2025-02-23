package com.server.delivery.domain.delivery.dto.res;

import java.util.UUID;

import com.server.delivery.model.delivery.entity.Delivery;
import com.server.delivery.model.order.entity.Order;
import com.server.delivery.model.order.entity.OrderStatus;

public record DeliveryCompleteResponseDto(
	OrderInfo orderInfo,
	UUID deliveryId
) {
	public static DeliveryCompleteResponseDto from(Delivery delivery, Order order) {
		return new DeliveryCompleteResponseDto(
			OrderInfo.from(order),
			delivery.getDeliveryUuid()
		);
	}

	public record OrderInfo(
		UUID orderUuid,
		OrderStatus orderStatus
	) {
		public static OrderInfo from(Order order) {
			return new OrderInfo(
				order.getOrderUuid(),
				order.getOrderStatus()
			);
		}
	}
}

