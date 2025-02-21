package com.server.delivery.model.store.entity;

import com.server.delivery.common.BaseEntity;
import com.server.delivery.model.menu.entity.Menu;
import com.server.delivery.model.order.entity.Order;
import com.server.delivery.model.owner.entity.OwnerStore;
import com.server.delivery.model.review.entity.Review;
import com.server.delivery.model.user.entity.UserStore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
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
    private boolean storeIsDeleted = Boolean.FALSE;

    @Column(name = "store_is_granted")
    private boolean storeIsGranted = Boolean.FALSE;

    @OneToMany(mappedBy = "store")
    private List<UserStore> userStore;

    @OneToMany(mappedBy = "store")
    private List<Menu> menus;

    @OneToMany(mappedBy = "store")
    private List<Order> orders;

    @OneToMany(mappedBy = "store")
    private List<StoreOperationTimes> operatingHours;

    @Builder.Default
    @OneToMany(mappedBy = "store")
    private List<Review> reviews = new ArrayList<>();

    //StoreCategoryMapping을 통해 매장과 카테고리를 연결
    @OneToMany(mappedBy = "store")
    private List<StoreCategoryMapping> categoryMappings = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "location_uuid")
    private Location location;

    @OneToMany(mappedBy = "store")
    private List<OwnerStore> ownerStore;
}
