package com.server.delivery.domain.order.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRejectRequestDto {
    private String orderAcceptStatus;
    private String rejectMessage;
}
