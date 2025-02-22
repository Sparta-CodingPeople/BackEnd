package com.server.delivery.domain.review.dto.req;

import java.util.UUID;

public record ReviewCreateRequestDto(
	UUID orderId,
	UUID storeUuid,
	String content,
	Double rating
) {
}
