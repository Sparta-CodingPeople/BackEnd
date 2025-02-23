package com.server.delivery.model.menu.entity;

import com.server.delivery.common.BaseEntity;
import com.server.delivery.model.cart.entity.MenuCart;
import com.server.delivery.model.store.entity.Store;
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
@SQLDelete(sql = "UPDATE p_menus SET menus_availability = FALSE WHERE menus_uuid = ?")
@SQLRestriction("menus_availability = true")
@Table(name = "p_menus")
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "menus_uuid")
    private UUID menuUuId;

    @Column(name = "menus_name", nullable = false)
    private String menuName;

    @Column(name = "menus_description")
    private String menuDescription;

    @Column(name = "menus_price", nullable = false)
    private int menuPrice;

    @Column(name = "menus_availability", nullable = false)
    @Builder.Default
    private Boolean menuAvailability = Boolean.TRUE;

    @Column(name = "food_image")
    private String foodImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    // todo. 필요 시 추가
//    @OneToMany(mappedBy = "menu")
//    private List<Review> reviews;

    @OneToMany(mappedBy = "menu")
    private List<MenuCart> menuCarts;

}
