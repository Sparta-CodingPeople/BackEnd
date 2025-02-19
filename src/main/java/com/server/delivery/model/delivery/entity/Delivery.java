package com.server.delivery.model.delivery.entity;

import com.server.delivery.common.BaseEntity;
import com.server.delivery.model.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE p_delivery SET deleted_at = true WHERE delivery_id = ?")
@SQLRestriction("deleted_at is null") // deleted_at이 null인 데이터만 조회
@Table(name = "p_delivery")
public class Delivery extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "delivery_id")
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status")
    private DeliveryStatus status;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "delivery_memo")
    private String deliveryMemo;

    @Column(name = "delivery_start_time")
    private LocalDateTime deliveryStartTime;

    @Column(name = "delivery_estimated_time")
    private LocalDateTime deliveryEstimatedTime;

    @Column(name = "delivery_cancel_reason")
    private String cancelReason;

    @OneToOne(mappedBy = "delivery")  // Order 엔티티의 delivery 필드를 참조
    private Order order;

}
