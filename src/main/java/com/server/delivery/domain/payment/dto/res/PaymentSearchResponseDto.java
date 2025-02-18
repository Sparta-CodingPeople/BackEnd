package com.server.delivery.domain.payment.dto.res;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentSearchResponseDto(
	String paymentId,
	String orderId,
	String transactionId,
	BigDecimal totalAmount,
	String paymentMethod,
	String status,
	LocalDateTime paidAt
) {
}
