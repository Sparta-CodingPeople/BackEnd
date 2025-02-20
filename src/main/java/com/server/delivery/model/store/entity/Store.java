package com.server.delivery.model.store.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.server.delivery.common.BaseEntity;
import com.server.delivery.model.menu.entity.Menu;
import com.server.delivery.model.order.entity.Order;
import com.server.delivery.model.review.entity.Review;
import com.server.delivery.model.user.entity.UserStore;

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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE p_store SET store_is_deleted = true WHERE store_uuid = ?")
@SQLRestriction("store_is_deleted = false")
@Table(name = "p_store")
public class Store extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "store_uuid")
	private UUID storeUuid;

	@Column(name = "store_name", nullable = false, length = 100)
	private String storeName;

	@Column(name = "phone_number", nullable = false, length = 100)
	private String phoneNumber;

	@Column(name = "store_description")
	private String storeDescription;

	@Column(name = "store_is_deleted")
	private boolean storeIsDeleted = Boolean.FALSE;

	@Column(name = "store_is_granted")
	private boolean storeIsGranted = Boolean.FALSE;

	@OneToMany(mappedBy = "store")
	private List<UserStore> userStore;

	@ManyToOne
	@JoinColumn(name = "store_category_id")
	private StoreCategory storeCategory;

	@OneToMany(mappedBy = "store")
	private List<Menu> menus;

	@OneToMany(mappedBy = "store")
	private List<Order> orders;

	@OneToMany(mappedBy = "store", fetch = FetchType.EAGER)
	private List<StoreOperationTimes> operatingHours;

	@Builder.Default
	@OneToMany(mappedBy = "store")
	private List<Review> reviews = new ArrayList<>();

	@OneToOne
	@JoinColumn(name = "store_location_uuid")
	private StoreLocation storeLocation;  // 이 부분에서 StoreLocation과 연결
}
