package com.server.delivery.model.order.entity;

import com.server.delivery.common.BaseEntity;
import com.server.delivery.common.exception.ExceptionCode;
import com.server.delivery.common.exception.customException.CustomDeliveryException;
import com.server.delivery.model.delivery.entity.Delivery;
import com.server.delivery.model.delivery.entity.DeliveryStatus;
import com.server.delivery.model.payment.Payment;
import com.server.delivery.model.review.entity.Review;
import com.server.delivery.model.store.entity.Store;
import com.server.delivery.model.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE p_orders SET orders_is_deleted = true WHERE orders_uuid = ?")
@SQLRestriction("orders_is_deleted = false")
@Table(name = "p_orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "orders_uuid")
    private UUID orderUuid;

    @Column(name = "orders_total_price")
    private int totalPrice;

    @Column(name = "orders_total_quantity")
    private int totalQuantity; //주문총수량 //item의 개수? 주문한 수량의 합?

    @Column(name = "orders_orders_message")
    private String orderMessage;

    @Column(name = "orders_orders_type")
    @Enumerated(EnumType.STRING)
    private OrderType orderType;  //온라인,오프라인 -> (배달/포장)

    @Column(name = "orders_orders_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 준비중/배달중/거절/승인/취소 !==딜리버리스테이터스

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_uuid")
    private Delivery delivery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_uuid")  // Store 엔티티와의 관계 설정
    private Store store;

    @OneToOne(mappedBy = "order")
    private Review review;

    // ref. 배달 조회 시 필요 (조리 시간)
    @Column(name = "orders_cooking_time")
    private Integer orderCookingTime;

    // ref. 배달 조회 시 필요 (배달 예상 시간)
    @Column(name = "orders_estimated_delivery_time")
    private Integer estimatedDeliveryTime;

    // ref. 배달 조회 시 필요 (배달 주소)
    @Column(name = "orders_delivery_address")
    private String deliveryAddress;

    @Builder.Default
    @Column(name = "orders_is_deleted")
    private Boolean isDeleted = Boolean.FALSE;

    @OneToMany(mappedBy = "order")
    private List<OrderMenu> orderMenus = new ArrayList<>();

    public void changeOrderStatusAfterPaymentCancel(Payment payment) {
        this.orderStatus = OrderStatus.CANCELED;
        this.payment = payment;
        payment.changeOrder(this);
    }


    public void changePayment(Payment payment) {
        this.payment = payment;
    }

    public boolean isNotDeliveryCompleted() {
        if (delivery == null) {
            throw new CustomDeliveryException(ExceptionCode.DELIVERY_NOT_FOUND);
        }
        return delivery.getStatus() != DeliveryStatus.COMPLETED;
    }
}
