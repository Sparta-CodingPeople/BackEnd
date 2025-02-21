package com.server.delivery.domain.payment.client.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.server.delivery.model.payment.PaymentMethod;
import com.server.delivery.model.payment.PaymentStatus;

public record PaymentConfirmOutput(
	String paymentKey,
	UUID serverOrderId,
	String transactionKey,
	BigDecimal totalAmount,
	PaymentMethod paymentMethod,
	ZonedDateTime requestedAt,
	ZonedDateTime approvedAt,
	PaymentStatus status
) {
}
