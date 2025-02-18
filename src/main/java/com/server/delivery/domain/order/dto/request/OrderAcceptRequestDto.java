package com.server.delivery.domain.order.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class OrderAcceptRequestDto {
    private UUID orderId;
    private String orderAcceptStatus;
    private LocalDateTime orderTime;
    private String deliveryType;
    private String cookingTime;
    private Timestamp estimatedDeliveryTime;
}
