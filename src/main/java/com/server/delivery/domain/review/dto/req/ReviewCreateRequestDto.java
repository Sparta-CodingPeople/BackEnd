package com.server.delivery.domain.review.dto.req;

public record ReviewCreateRequestDto(
	String orderId,
	String storeId,
	String content,
	Double rating
) {
}
