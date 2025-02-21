package com.server.delivery.model.order.entity;

import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.server.delivery.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@SQLDelete(sql = "UPDATE p_order_menu SET order_menu_is_deleted = true WHERE orders_menus_uuid = ?")
@SQLRestriction("order_menu_is_deleted = false")
@Table(name = "p_order_menu")
public class OrderMenu extends BaseEntity {
	@Id
	@Column(name = "orders_menus_uuid")
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "order_menu_quantity")
	private int quantity;

	@Column(name = "order_menu_total_price")
	private int totalPrice;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@Builder.Default
	@Column(name = "order_menu_is_Deleted")
	private Boolean isDeleted = Boolean.FALSE;

}
