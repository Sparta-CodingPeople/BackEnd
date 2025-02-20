package com.server.delivery.domain.review.dto.req;

public record ReviewUpdateRequestDto(
	String content,
	Double rating
) {
}
