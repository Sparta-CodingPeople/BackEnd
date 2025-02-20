package com.server.delivery.domain.review.dto.res;

import java.time.LocalDateTime;
import java.util.List;

import com.server.delivery.model.review.entity.Review;
import com.server.delivery.model.review.entity.ReviewImage;

public record ReviewDetailSearchResponseDto(
	Double rating,
	String content,
	List<String> images,
	LocalDateTime createdAt,
	String createdBy
) {
	public static ReviewDetailSearchResponseDto from(Review review) {
		return new ReviewDetailSearchResponseDto(
			review.getRating(),
			review.getContent(),
			review.getImages().stream().map(ReviewImage::getImageUrl).toList(),
			review.getCreatedAt(),
			review.getCreatedBy()
		);
	}
}
