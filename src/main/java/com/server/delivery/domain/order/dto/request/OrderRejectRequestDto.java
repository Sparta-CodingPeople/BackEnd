package com.server.delivery.domain.order.dto.request;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderRejectRequestDto {
	private String rejectMessage;
	private UUID paymentUuid;
}
