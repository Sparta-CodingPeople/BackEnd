package com.server.delivery.model.payment;

import com.server.delivery.model.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE p_payment SET payment_is_deleted = true WHERE payment_id = ?")
@SQLRestriction("payment_is_deleted = false")
@Table(name = "p_payment")
public class Payment {

    @OneToOne
    public Order order;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id")
    private String id;
    @Column(name = "payment_amount")
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus status;
    @Column(name = "payment_transaction_id")
    private String transactionId;
    @Column(name = "payment_is_deleted")
    @Builder.Default
    private Boolean isDeleted = Boolean.FALSE;
    @Column(name = "paid_at")
    private LocalDateTime paidAt;
    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;
}
