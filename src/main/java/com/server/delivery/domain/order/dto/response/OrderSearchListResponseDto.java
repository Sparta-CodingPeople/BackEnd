package com.server.delivery.domain.order.dto.response;

import com.server.delivery.model.order.entity.Order;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class OrderSearchListResponseDto {
    private UUID orderUuid;
    private String storeName;
    private int totalprice;
    private String orderType;
    private String payType;
    private LocalDateTime orderTime;
    private String payStatus;

    public static OrderSearchListResponseDto from(Order order) {
        // Payment가 null인지 확인
        if (order.getPayment() == null) {
            // Payment가 없을 경우 null로 반환하거나 기본값을 설정할 수 있음
            return OrderSearchListResponseDto.builder()
                    .orderUuid(order.getOrderUuid())
                    .orderTime(null) // 또는 적절한 기본값 설정
                    .storeName(order.getStore().getStoreName())
                    .orderType(order.getOrderType().getKoreanValue())
                    .payStatus(null) // 결제 정보가 없을 경우 상태를 설정
                    .payType(null) // 결제 정보가 없을 경우 상태를 설정
                    .totalprice(order.getTotalPrice())
                    .build();
        }

        // Payment가 null이 아닐 경우 정상적으로 처리
        LocalDateTime localDateTime = order.getPayment().getPaidAt().toLocalDateTime();
        String paystatus = order.getPayment().getStatus().name();
        String payMethod = order.getPayment().getPaymentMethod().name();

        return OrderSearchListResponseDto.builder()
                .orderUuid(order.getOrderUuid())
                .orderTime(localDateTime)
                .storeName(order.getStore().getStoreName())
                .orderType(order.getOrderType().getKoreanValue())
                .payStatus(paystatus.isEmpty() ? paystatus : null)
                .payType(payMethod.isEmpty() ? paystatus : null)
                .totalprice(order.getTotalPrice())
                .build();
    }
}
