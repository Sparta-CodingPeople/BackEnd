package com.server.delivery.domain.review.dto.res;

import java.time.LocalDateTime;
import java.util.List;

public record StoreReviewSearchResponseDto(
	Long reviewId,
	Long userId,
	Double rating,
	String content,
	List<String> images,
	LocalDateTime createdAt,
	String createdBy
) {
}
