package com.server.delivery.domain.delivery.dto.res;

import java.time.LocalDateTime;

public record DeliveryUpdateResponseDto(
	String orderId,
	String deliveryStatus,
	LocalDateTime deliveryStartTime,
	LocalDateTime deliveryEstimatedTim
) {
}
