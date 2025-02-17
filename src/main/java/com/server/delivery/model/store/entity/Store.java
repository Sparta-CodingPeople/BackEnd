package com.server.delivery.model.store.entity;

import com.server.delivery.common.BaseEntity;
import com.server.delivery.model.menu.entity.Menu;
import com.server.delivery.model.user.entity.Owner;
import com.server.delivery.model.user.entity.User;
import jakarta.persistence.*;
import lombok.*;


import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_store")
public class Store extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "store_id")
    private UUID storeId;

    @Column(name = "store_name", nullable = false, length = 100)
    private String storeName;

    @Column(name = "contact_number", nullable = false, length = 100)
    private String contactNumber;

    @Column(name = "is_deleted")
    private boolean isDeleted = Boolean.FALSE;

    @Column(name = "is_granted")
    private boolean isGranted = Boolean.FALSE;

    @ManyToOne
    @JoinColumn(name = "owner_uuid")
    private Owner owner;

    @ManyToOne
    @JoinColumn(name = "manager_uuid")
    private User manager;

    @ManyToOne
    @JoinColumn(name = "store_category_id")
    private StoreCategory storeCategory;

    @OneToMany(mappedBy = "store")
    private List<Menu> menus;

//    @OneToMany(mappedBy = "store")
//    private List<Order> orders;

    @OneToMany(mappedBy = "store")
    private List<StoreOperatingHours> operatingHours;
}
