package com.server.delivery.domain.review.dto.req;

import java.util.UUID;

public record ReviewCreateRequestDto(
	UUID orderId,
	// ref. foundOrder.getStore();로 조회 vs storeId로 조회
	UUID storeId,
	String content,
	Double rating
) {
}
