package com.server.delivery.domain.order.dto.request;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderAcceptRequestDto {
	//private String orderAcceptStatus;
	private LocalDateTime orderTime;
	//private String deliveryType;
	// ref. 조리 시간과 예상 시간을 더해야 해서 Integer 타입으로 변경
	private Integer cookingTime;
	private Integer estimatedDeliveryTime;
}
