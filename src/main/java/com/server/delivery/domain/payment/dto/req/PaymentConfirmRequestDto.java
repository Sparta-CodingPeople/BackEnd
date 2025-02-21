package com.server.delivery.domain.payment.dto.req;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentConfirmRequestDto(
	String paymentKey,
	UUID serverOrderId,
	BigDecimal amount
) {
}

