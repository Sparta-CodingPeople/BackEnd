package com.server.delivery.domain.review.dto.res;

import java.time.LocalDateTime;
import java.util.List;

public record UserReviewSearchResponseDto(
	Long reviewId,
	Long storeId,
	String storeName,
	Double rating,
	String content,
	List<String> images,
	LocalDateTime createdAt
) {
}
