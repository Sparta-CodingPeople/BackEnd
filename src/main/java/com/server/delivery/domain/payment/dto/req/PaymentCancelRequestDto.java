package com.server.delivery.domain.payment.dto.req;

import java.math.BigDecimal;

public record PaymentCancelRequestDto(
	String cancelReason,
	BigDecimal cancelAmount
) {
}
