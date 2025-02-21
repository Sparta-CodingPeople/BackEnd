package com.server.delivery.model.order.entity;

import java.util.List;
import java.util.UUID;

import com.server.delivery.model.delivery.entity.DeliveryStatus;
import com.server.delivery.model.payment.Payment;
import com.server.delivery.model.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.server.delivery.common.BaseEntity;
import com.server.delivery.model.delivery.entity.Delivery;
import com.server.delivery.model.review.entity.Review;
import com.server.delivery.model.store.entity.Store;

@Entity
@Getter
@Setter
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
	private int totalQuantity; //주문총수량 //item의 개수? 주문한 수량의 합?

	@Column(name = "order_order_message")
	private String OrderMessage;

	@Column(name = "order_order_type")
	@Enumerated(EnumType.STRING)
	private OrderType orderType;  //온라인,오프라인

	@Column(name = "order_order_status")
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus; // 준비중/배달중/배달완료 !==딜리버리스테이터스

	@OneToOne
	@JoinColumn(name = "payment_id")
	private Payment payment;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToOne
	@JoinColumn(name = "delivery_id")
	private Delivery delivery;

	@ManyToOne
	@JoinColumn(name = "store_id")  // Store 엔티티와의 관계 설정
	private Store store;

	@OneToOne(mappedBy = "order")
	private Review review;

	@Builder.Default
	@Column(name = "order_is_Deleted")
	private Boolean isDeleted = Boolean.FALSE;

	@OneToMany(mappedBy="order",fetch=FetchType.LAZY)
	private List<OrderMenu> orderMenus;

}
