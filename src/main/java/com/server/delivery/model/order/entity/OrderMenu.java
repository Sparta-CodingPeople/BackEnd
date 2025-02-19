package com.server.delivery.model.order.entity;


import com.server.delivery.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("where is_deleted = false")
@Table(name = "p_order_menu")

public class OrderMenu extends BaseEntity {
    @Id
    @Column(name = "orders_menus_uuid")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String menuId;

    @Column(name = "order_menu_quantity")
    private int quantity;

    @Column(name = "order_menu_total_price")
    private int totalPrice;

}
