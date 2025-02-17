package com.server.delivery.domain.review.dto.req;

public record ReviewUpdateRequestDto(
	Long orderId,
	Long storeId,
	String comment,
	Double rating
) {
}
