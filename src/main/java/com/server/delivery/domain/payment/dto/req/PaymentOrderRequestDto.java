package com.server.delivery.domain.payment.dto.req;

import java.math.BigDecimal;

public record PaymentOrderRequestDto(
	String orderId,
	String orderName,
	BigDecimal totalAmount,
	CardInfo cardInfo
) {
	public record CardInfo(
		String cardNumber,
		String expirationMonth,
		String expirationYear,
		String cvv
	) {
	}
}
