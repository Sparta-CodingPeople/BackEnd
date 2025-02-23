package com.server.delivery.domain.order.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderAcceptRequestDto {
	private LocalDateTime orderTime;
	private Integer cookingTime;
	private Integer estimatedDeliveryTime;
	private UUID paymentUuid;
}
