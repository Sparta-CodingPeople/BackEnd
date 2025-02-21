package com.server.delivery.model.order.entity;

import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.server.delivery.common.BaseEntity;
import com.server.delivery.model.delivery.entity.Delivery;
import com.server.delivery.model.payment.Payment;
import com.server.delivery.model.review.entity.Review;
import com.server.delivery.model.store.entity.Store;
import com.server.delivery.model.user.entity.User;

import jakarta.persistence.Column;
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
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE p_order SET order_is_Deleted = true WHERE order_uuid = ?")
@SQLRestriction("order_is_deleted = false")
@Table(name = "p_order")
public class Order extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "order_uuid")
	private UUID orderId;

	@Column(name = "order_total_price")
	private int totalPrice;

	@Column(name = "order_total_quantity")
	private int totalQuantity;

	@Column(name = "order_order_type")
	@Enumerated(EnumType.STRING)
	private OrderType orderType;

	@Column(name = "order_order_status")
	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@OneToOne
	@JoinColumn(name = "delivery_uuid")
	private Delivery delivery;

	@ManyToOne
	@JoinColumn(name = "store_uuid")  // Store 엔티티와의 관계 설정
	private Store store;

	@OneToOne(mappedBy = "order")
	private Review review;

	@OneToOne(mappedBy = "order")
	private Payment payment;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Builder.Default
	@Column(name = "order_is_Deleted")
	private Boolean isDeleted = Boolean.FALSE;
}
