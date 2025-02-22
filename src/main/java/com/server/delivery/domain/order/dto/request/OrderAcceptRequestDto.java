package com.server.delivery.domain.order.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class OrderAcceptRequestDto {
    private LocalDateTime orderTime;
    // ref. 조리 시간과 예상 시간을 더해야 해서 Integer 타입으로 변경
    private Integer cookingTime;
    private Integer estimatedDeliveryTime;
}
