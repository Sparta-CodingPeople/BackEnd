package com.server.delivery.domain.delivery.dto.req;

import java.time.LocalDateTime;

public record DeliveryUpdateRequestDto(
	String deliveryStatus,
	LocalDateTime deliveryStartTime
) {
}
