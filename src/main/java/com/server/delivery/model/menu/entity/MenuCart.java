package com.server.delivery.model.menu.entity;

import com.server.delivery.model.cart.entity.Cart;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MenuCart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;

    @Column(name = "menu_cart_quantity", nullable = false)
    private int quantity;

    @Column(name = "menu_cart_total_price", nullable = false)
    private int totalPrice;

}
