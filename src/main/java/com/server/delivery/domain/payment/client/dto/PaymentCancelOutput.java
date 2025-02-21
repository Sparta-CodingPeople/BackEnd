package com.server.delivery.domain.payment.client.dto;

import java.time.ZonedDateTime;

import com.server.delivery.model.payment.PaymentStatus;

public record PaymentCancelOutput(
	String paymentKey,
	String lastTransactionKey,
	PaymentStatus status,
	Cancels cancels
) {
	public record Cancels(
		String cancelTransactionKey,
		String cancelReason,
		ZonedDateTime canceledAt
	) {
	}
}
