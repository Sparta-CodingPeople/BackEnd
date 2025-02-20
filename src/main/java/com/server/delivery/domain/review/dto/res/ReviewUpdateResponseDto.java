package com.server.delivery.domain.review.dto.res;

import java.time.LocalDateTime;
import java.util.List;

import com.server.delivery.model.review.entity.Review;
import com.server.delivery.model.review.entity.ReviewImage;

public record ReviewUpdateResponseDto(
	Double rating,
	String content,
	List<String> images,
	LocalDateTime createdAt,
	String createdBy,
	LocalDateTime modifiedAt,
	String modifiedBy
) {
	public static ReviewUpdateResponseDto from(Review review) {
		return new ReviewUpdateResponseDto(
			review.getRating(),
			review.getContent(),
			review.getImages().stream().map(ReviewImage::getImageUrl).toList(),
			review.getCreatedAt(),
			review.getCreatedBy(),
			review.getModifiedAt(),
			review.getModifiedBy()
		);
	}
}
