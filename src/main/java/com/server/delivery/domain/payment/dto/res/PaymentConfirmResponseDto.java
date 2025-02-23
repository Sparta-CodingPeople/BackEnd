package com.server.delivery.domain.payment.dto.res;

import java.util.UUID;

import com.server.delivery.model.payment.Payment;

public record PaymentConfirmResponseDto(
	UUID paymentId
) {
	public static PaymentConfirmResponseDto from(Payment payment) {
		return new PaymentConfirmResponseDto(payment.getPaymentUuid());
	}
}



