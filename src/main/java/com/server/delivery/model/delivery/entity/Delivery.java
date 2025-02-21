package com.server.delivery.model.delivery.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.server.delivery.common.BaseEntity;
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
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE p_delivery SET delivery_is_deleted = true WHERE delivery_id = ?")
@SQLRestriction("delivery_is_deleted = false")
@Table(name = "p_delivery")
public class Delivery extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "delivery_id")
	private UUID id;

	@Enumerated(EnumType.STRING)
	@Column(name = "delivery_status")
	private DeliveryStatus status;

	@Column(name = "delivery_start_time")
	private LocalDateTime deliveryStartTime;

	@Column(name = "delivery_arrival_time") // 도착 예상 시간
	private Integer deliveryArrivalTime;

	@Column(name = "delivery_cancel_reason")
	private String cancelReason;

	@OneToOne(mappedBy = "delivery")
	private Order order;

	@Enumerated(EnumType.STRING)
	private DeliveryTip deliveryTip;

	@Builder.Default
	@Column(name = "delivery_is_deleted")
	private Boolean isDeleted = Boolean.FALSE;

	public void isDeleted() {
		this.isDeleted = Boolean.TRUE;
		this.setDeletedAt(LocalDateTime.now());
	}

	public void updateDeliveryStartTime(Order order) {
		this.deliveryStartTime = order.getCreatedAt();
	}

	public void updateDeliveryEstimatedTime(Order foundOrder) {
		int orderCookingTime = foundOrder.getOrderCookingTime();
		int estimatedDeliveryTime = foundOrder.getEstimatedDeliveryTime();
		this.deliveryArrivalTime = orderCookingTime + estimatedDeliveryTime;
	}
}
