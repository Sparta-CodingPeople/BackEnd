package com.server.delivery.domain.order.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderRejectRequestDto {
    private String orderAcceptStatus;
    private String rejectMessage;
}
