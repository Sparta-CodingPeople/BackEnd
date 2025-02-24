package com.server.delivery.model.payment;

import com.server.delivery.domain.payment.client.dto.PaymentCancelOutput;
import com.server.delivery.model.order.entity.Order;
import com.server.delivery.model.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "p_payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id")
    private UUID paymentUuid;

    @Column(name = "payment_amount")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "payment_transaction_id", unique = true)
    private String transactionKey;

    @Column(unique = true)
    private String paymentKey;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_uuid")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uuid")
    private User user;

    @Column(name = "payment_request_at")
    private ZonedDateTime requestedAt;

    @Column(name = "payment_paid_at")
    private ZonedDateTime paidAt;

    @Embedded
    private PaymentCancelDetails cancelDetails;

    public void changeCancelStatus(PaymentCancelOutput paymentCancelOutput) {
        // orderStatus는 canceled로 변경되고, paymentStatus는 canceled로 설정
        this.order.changeOrderStatusAfterPaymentCancel(this);
        this.paymentKey = paymentCancelOutput.paymentKey();
        this.transactionKey = paymentCancelOutput.lastTransactionKey();
        this.status = paymentCancelOutput.status();
        this.cancelDetails = PaymentCancelDetails.builder()
                .canceledAt(paymentCancelOutput.cancels().canceledAt())
                .cancelReason(paymentCancelOutput.cancels().cancelReason())
                .cancelTransactionKey(paymentCancelOutput.cancels().cancelTransactionKey())
                .build();
    }

    public boolean isCanceled() {
        return this.cancelDetails != null;
    }

    public void changeOrder(Order order) {
        this.order = order;
    }
}
