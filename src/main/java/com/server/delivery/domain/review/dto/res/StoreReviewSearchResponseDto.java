package com.server.delivery.domain.review.dto.res;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.server.delivery.model.review.entity.Review;
import com.server.delivery.model.review.entity.ReviewImage;

public record StoreReviewSearchResponseDto(
	UUID reviewId,
	Long userId,
	Double rating,
	String content,
	List<String> images,
	LocalDateTime createdAt,
	String createdBy
) {
	public static StoreReviewSearchResponseDto from(Review review) {
		return new StoreReviewSearchResponseDto(
			review.getId(),
			review.getUser().getId(),
			review.getRating(),
			review.getContent(),
			review.getImages().stream().map(ReviewImage::getImageUrl).toList(),
			review.getCreatedAt(),
			review.getCreatedBy()
		);
	}
}
