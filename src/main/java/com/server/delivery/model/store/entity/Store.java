package com.server.delivery.model.store.entity;

import com.server.delivery.common.BaseEntity;
import com.server.delivery.model.manager.entity.Manager;
import com.server.delivery.model.menu.entity.Menu;
import com.server.delivery.model.order.entity.Order;
import com.server.delivery.model.owner.entity.Owner;
import com.server.delivery.model.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;
import java.util.UUID;

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
    @Builder.Default
    private boolean storeIsDeleted = Boolean.FALSE;

    @Column(name = "store_is_granted")
    @Builder.Default
    private boolean storeIsGranted = Boolean.FALSE;

    @OneToMany(mappedBy = "store")
    private List<Menu> menus;

    @OneToMany(mappedBy = "store")
    private List<Order> orders;

    @OneToMany(mappedBy = "store")
    private List<StoreOperationTimes> operatingHours;

    @OneToMany(mappedBy = "store")
    private List<Review> reviews;

    //StoreCategoryMapping을 통해 매장과 카테고리를 연결
    @OneToMany(mappedBy = "store")
    private List<StoreCategoryMapping> categoryMappings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_uuid")
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)  // 하나의 매장은 한 명의 점주만 가질 수 있음
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @OneToOne(mappedBy = "store")  // 매장에 매니저 한 명만 가능
    private Manager manager;  // 추가된 부분
}
