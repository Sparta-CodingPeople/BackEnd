package com.server.delivery.model.order.entity;

import com.server.delivery.common.BaseEntity;
import com.server.delivery.model.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE p_orders_menu SET orders_menus_is_deleted = true WHERE orders_menus_uuid = ?")
@SQLRestriction("orders_menus_is_deleted = false")
@Table(name = "p_orders_menu")
public class OrderMenu extends BaseEntity {
    @Id
    @Column(name = "orders_menus_uuid")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "orders_menus_quantity")
    private int quantity;

    @Column(name = "orders_menus_total_price")
    private int totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_uuid")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menus_uuid")
    private Menu menu;

    @Builder.Default
    @Column(name = "orders_menus_is_Deleted")
    private Boolean isDeleted = Boolean.FALSE;

}
