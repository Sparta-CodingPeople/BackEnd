package com.server.delivery.domain.delivery.dto.req;

import java.util.UUID;

public record DeliveryStartRequestDto(UUID orderUuid) {
}
