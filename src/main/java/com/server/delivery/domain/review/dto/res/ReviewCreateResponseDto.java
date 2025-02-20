package com.server.delivery.domain.review.dto.res;

import java.util.UUID;

public record ReviewCreateResponseDto(
	UUID reviewId
) {
	public static ReviewCreateResponseDto from(UUID reviewId) {
		return new ReviewCreateResponseDto(reviewId);
	}
}
