package com.server.delivery.domain.delivery.dto.res;

import java.time.LocalDateTime;

public record DeliverySearchResponseDto(
	String orderId,
	String deliveryAddress,
	String deliveryStatus,
	LocalDateTime deliveryStartTime,
	LocalDateTime estimatedDeliveryTime
) {
}
