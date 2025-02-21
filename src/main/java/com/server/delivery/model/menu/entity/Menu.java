package com.server.delivery.model.menu.entity;

import com.server.delivery.common.BaseEntity;
import com.server.delivery.model.cart.entity.MenuCart;
import com.server.delivery.model.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE p_menus SET menu_availability = FALSE WHERE menu_uuid = ?")
@Table(name = "p_menus")
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "menu_uuid")
    private UUID menuUuId;

    @Column(name = "menu_name", nullable = false)
    private String menuName;

    @Column(name = "menu_description")
    private String menuDescription;

    @Column(name = "menu_price", nullable = false)
    private int menuPrice;

    @Column(name = "menu_availability", nullable = false)
    private boolean menuAvailability;

    @Column(name = "food_image")
    private String foodImage;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    // todo. 필요 시 추가
//    @OneToMany(mappedBy = "menu")
//    private List<Review> reviews;

    @OneToMany(mappedBy = "menu")
    private List<MenuCart> menuCarts;

    public void softDelete() {
        this.setDeletedAt(LocalDateTime.now());
        this.setDeletedBy(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
