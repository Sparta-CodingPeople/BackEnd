package com.server.delivery.model.store.entity;

import com.server.delivery.common.BaseEntity;
import com.server.delivery.model.menu.entity.Menu;
import com.server.delivery.model.user.entity.Owner;
import com.server.delivery.model.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;


import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE p_store SET store_is_deleted = true WHERE store_uuid = ?")
@Table(name = "p_store")
public class Store extends BaseEntity{

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

    @ManyToOne
    @JoinColumn(name = "user_uuid", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_category_id")
    private StoreCategory storeCategory;

    @OneToMany(mappedBy = "store")
    private List<Menu> menus;

//    @OneToMany(mappedBy = "store")
//    private List<Order> orders;

    @OneToMany(mappedBy = "store")
    private List<StoreOperationTimes> operatingHours;
}
