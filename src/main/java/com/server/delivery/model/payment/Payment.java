package com.server.delivery.model.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.server.delivery.model.order.entity.Order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@SQLDelete(sql = "UPDATE p_payment SET payment_is_deleted = true WHERE payment_id = ?")
@SQLRestriction("payment_is_deleted = false")
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
	private PaymentStatus status;

	@Column(name = "payment_transaction_id")
	private String transactionId;

	@OneToOne
	private Order order;

	@Column(name = "payment_is_deleted")
	@Builder.Default
	private Boolean isDeleted = Boolean.FALSE;

	@Column(name = "paid_at")
	private LocalDateTime paidAt;
    
	@Column(name = "canceled_at")
	private LocalDateTime canceledAt;
}
