package com.server.delivery.model.cart.entity;

import com.server.delivery.model.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MenuCart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "menus_cart_uuid")
    private UUID menuCartUuid;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;

    @Column(name = "menus_cart_quantity", nullable = false)
    private int quantity;

    @Column(name = "menus_cart_total_price", nullable = false)
    private int totalPrice;

}
