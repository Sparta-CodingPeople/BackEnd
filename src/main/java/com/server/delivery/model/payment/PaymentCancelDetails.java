package com.server.delivery.model.payment;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCancelDetails {
	@Column(name = "payment_canceled_at")
	private ZonedDateTime canceledAt;

	@Column(name = "payment_cancel_reason")
	private String cancelReason;

	@Column(name = "payment_cancel_transaction_Ic")
	private String cancelTransactionKey;
}
