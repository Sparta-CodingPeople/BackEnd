package com.server.delivery.model.delivery.entity;

import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.server.delivery.common.BaseEntity;
import com.server.delivery.model.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@SQLDelete(sql = "UPDATE p_delivery SET delivery_address_is_deleted = true WHERE delivery_address_id = ?")
@SQLRestriction("delivery_address_is_deleted = false")
@Table(name = "p_delivery_address")
public class DeliveryAddress extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "delivery_address_id")
	private UUID deliveryAddressUuid;

	@Column(name = "delivery_zipcode")
	private String zipcode;

	@Column(name = "delivery_city")
	private String city;

	@Column(name = "delivery_detail_address")
	private String detailAddress;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Builder.Default
	@Column(name = "delivery_address_is_deleted")
	private Boolean isDeleted = Boolean.FALSE;
}
