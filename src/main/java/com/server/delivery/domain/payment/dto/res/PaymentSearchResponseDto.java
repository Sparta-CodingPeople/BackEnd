package com.server.delivery.domain.payment.dto.res;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.server.delivery.model.payment.Payment;
import com.server.delivery.model.payment.PaymentCancelDetails;
import com.server.delivery.model.payment.PaymentMethod;
import com.server.delivery.model.payment.PaymentStatus;

public record PaymentSearchResponseDto(
	UUID paymentId,
	UUID orderId,
	String transactionKey,
	BigDecimal totalAmount,
	PaymentMethod paymentMethod,
	PaymentStatus status,
	LocalDateTime orderedAt,
	ZonedDateTime paidAt,
	ZonedDateTime canceledAt
) {
	public static PaymentSearchResponseDto from(Payment payment) {
		PaymentCancelDetails cancelDetails = payment.getCancelDetails();
		return new PaymentSearchResponseDto(
			payment.getId(),
			payment.getOrder().getOrderId(), // todo. n + 1
			payment.getTransactionKey(),
			payment.getAmount(),
			payment.getPaymentMethod(),
			payment.getStatus(),
			payment.getOrder().getCreatedAt(),// todo. n + 1
			payment.getPaidAt(),
			// ref. cancelDetails는 임베디드 타입 -> null을 반환하지 않도록 리팩토링 필요!
			cancelDetails != null ? cancelDetails.getCanceledAt() : null
		);
	}
}
