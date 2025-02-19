package com.server.delivery.model.order.entity;

import com.server.delivery.common.BaseEntity;
import com.server.delivery.model.delivery.entity.Delivery;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("where is_deleted = false")
@Table(name = "p_order")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_uuid")
    private String orderId;

    @Column(name = "order_total_price")
    private int totalPrice;

    @Column(name = "order_total_quantity")
    private int totalQuantity;

    @Column(name = "order_order_type")
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @Column(name = "order_order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
