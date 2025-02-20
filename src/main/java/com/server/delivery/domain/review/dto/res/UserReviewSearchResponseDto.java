package com.server.delivery.domain.review.dto.res;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.server.delivery.model.review.entity.Review;
import com.server.delivery.model.review.entity.ReviewImage;

public record UserReviewSearchResponseDto(
	UUID reviewId,
	//UUID storeId,
	//String storeName,
	Double rating,
	String content,
	List<String> images,
	LocalDateTime createdAt,
	String createdBy
) {
	public static UserReviewSearchResponseDto from(Review review) {
		return new UserReviewSearchResponseDto(
			review.getId(),
			//review.getStore().getStoreUuid(),
			//review.getStore().getStoreName(),
			review.getRating(),
			review.getContent(),
			review.getImages().stream().map(ReviewImage::getImageUrl).toList(),
			review.getCreatedAt(),
			review.getCreatedBy()
		);
	}
}
