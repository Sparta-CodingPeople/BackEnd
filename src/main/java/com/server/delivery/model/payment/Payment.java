package com.server.delivery.model.payment;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.server.delivery.domain.payment.client.dto.PaymentCancelOutput;
import com.server.delivery.model.order.entity.Order;
import com.server.delivery.model.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "p_payment")
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "payment_id")
	private UUID id;

	@Column(name = "payment_amount")
	private BigDecimal amount;

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_method")
	private PaymentMethod paymentMethod;

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_status")
	private PaymentStatus status = PaymentStatus.PENDING;

	@Column(name = "payment_transaction_id", unique = true)
	private String transactionKey;

	@Column(unique = true)
	private String paymentKey;

	@OneToOne
	@JoinColumn(name = "order_uuid")
	private Order order;

	@ManyToOne
	@JoinColumn(name = "user_uuid")
	private User user;

	@Column(name = "payment_request_at")
	private ZonedDateTime requestedAt;

	@Column(name = "payment_paid_at")
	private ZonedDateTime paidAt;

	@Embedded
	private PaymentCancelDetails cancelDetails;

	public void changeCancelStatus(PaymentCancelOutput paymentCancelOutput) {
		this.paymentKey = paymentCancelOutput.paymentKey();
		this.transactionKey = paymentCancelOutput.lastTransactionKey();
		this.status = paymentCancelOutput.status();
		this.cancelDetails = PaymentCancelDetails.builder()
			.canceledAt(paymentCancelOutput.cancels().canceledAt())
			.cancelReason(paymentCancelOutput.cancels().cancelReason())
			.cancelTransactionKey(paymentCancelOutput.cancels().cancelTransactionKey())
			.build();
	}

	public boolean isCanceled() {
		return cancelDetails.getCancelTransactionKey() != null;
	}
}
