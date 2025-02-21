package com.server.delivery.domain.payment.dto.res;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.server.delivery.model.payment.Payment;
import com.server.delivery.model.payment.PaymentCancelDetails;

public record PaymentCancelResponseDto(
	UUID paymentId,
	String paymentKey,
	ZonedDateTime canceledAt,
	String lastTransactionKey,
	String cancelTransactionKey
) {
	public static PaymentCancelResponseDto from(Payment payment) {
		PaymentCancelDetails cancelDetails = payment.getCancelDetails();
		return new PaymentCancelResponseDto(
			payment.getId(),
			payment.getPaymentKey(),
			cancelDetails.getCanceledAt(),
			payment.getTransactionKey(),
			cancelDetails.getCancelTransactionKey()
		);
	}
}
