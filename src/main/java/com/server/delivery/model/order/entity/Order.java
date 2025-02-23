package com.server.delivery.model.order.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.server.delivery.common.BaseEntity;
import com.server.delivery.common.exception.ExceptionCode;
import com.server.delivery.common.exception.customException.CustomDeliveryException;
import com.server.delivery.model.delivery.entity.Delivery;
import com.server.delivery.model.delivery.entity.DeliveryStatus;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE p_order SET order_is_deleted = true WHERE order_uuid = ?")
@SQLRestriction("order_is_deleted = false")
@Table(name = "p_order")
public class Order extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "order_uuid")
	private UUID orderUuid;

	@Column(name = "order_total_price")
	private int totalPrice;

	@Column(name = "order_total_quantity")
	private int totalQuantity; //주문총수량 //item의 개수? 주문한 수량의 합?

	@Column(name = "order_order_message")
	private String orderMessage;

	@Column(name = "order_order_type")
	@Enumerated(EnumType.STRING)
	private OrderType orderType;  //온라인,오프라인 -> (배달/포장)

	@Column(name = "order_order_status")
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus; // 준비중/배달중/거절/승인/취소 !==딜리버리스테이터스

	@OneToOne
	@JoinColumn(name = "payment_id")
	private Payment payment;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToOne
	@JoinColumn(name = "delivery_uuid")
	private Delivery delivery;

	@ManyToOne
	@JoinColumn(name = "store_uuid")  // Store 엔티티와의 관계 설정
	private Store store;

	@OneToOne(mappedBy = "order")
	private Review review;

	// ref. 배달 조회 시 필요 (조리 시간)
	@Column(name = "order_cooking_time")
	private Integer orderCookingTime;

	// ref. 배달 조회 시 필요 (배달 예상 시간)
	@Column(name = "order_estimated_delivery_time")
	private Integer estimatedDeliveryTime;

	// ref. 배달 조회 시 필요 (배달 주소)
	@Column(name = "order_delivery_address")
	private String deliveryAddress;

	@Builder.Default
	@Column(name = "order_is_deleted")
	private Boolean isDeleted = Boolean.FALSE;

	@OneToMany(mappedBy = "order")
	private List<OrderMenu> orderMenus = new ArrayList<>();

	public void changeOrderStatusAfterPaymentCancel(Payment payment) {
		this.orderStatus = OrderStatus.CANCELED;
		this.payment = payment;
		payment.changeOrder(this);
	}

	public void changePayment(Payment payment) {
		this.payment = payment;
	}

	public boolean isNotDeliveryCompleted() {
		if (delivery == null) {
			throw new CustomDeliveryException(ExceptionCode.DELIVERY_NOT_FOUND);
		}
		return delivery.getStatus() != DeliveryStatus.COMPLETED;
	}
}
