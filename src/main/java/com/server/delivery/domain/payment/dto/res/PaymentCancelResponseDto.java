package com.server.delivery.domain.payment.dto.res;

import java.time.LocalDateTime;

public record PaymentCancelResponseDto(
	String paymentId,
	String status,
	LocalDateTime canceledAt
) {
}
