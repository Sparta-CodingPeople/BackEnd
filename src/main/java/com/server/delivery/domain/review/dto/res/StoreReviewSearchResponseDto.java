package com.server.delivery.domain.review.dto.res;

import com.server.delivery.model.review.entity.Review;
import com.server.delivery.model.review.entity.ReviewImage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record StoreReviewSearchResponseDto(
        UUID reviewId,
        Long userId,
        Double rating,
        String content,
        List<String> images,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {
    public static StoreReviewSearchResponseDto from(Review review) {
        return new StoreReviewSearchResponseDto(
                review.getReviewUuid(),
                review.getUser().getId(),
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
