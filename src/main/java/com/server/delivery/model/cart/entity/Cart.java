package com.server.delivery.model.cart.entity;

import com.server.delivery.common.BaseEntity;
import com.server.delivery.model.menu.entity.MenuCart;
import com.server.delivery.model.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("where is_deleted = false")
@Table(name = "p_cart")
public class Cart extends BaseEntity {
    @Id
    @Column(name = "carts_uuid")
    private String cartId;

    @Column(name = "total_quantity")
    private int totalQuantity;

    @Column(name = "total_price")
    private int totalPrice;

    @OneToMany(mappedBy = "cart")
    private List<MenuCart> menuCarts;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "cart_is_deleted")
    private Boolean is_deleted = Boolean.FALSE;
}
