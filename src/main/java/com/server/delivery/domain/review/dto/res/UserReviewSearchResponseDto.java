package com.server.delivery.domain.review.dto.res;

import com.server.delivery.model.review.entity.Review;
import com.server.delivery.model.review.entity.ReviewImage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UserReviewSearchResponseDto(
        UUID reviewId,
        UUID storeUuid,
        String storeName,
        Double rating,
        String content,
        List<String> images,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {
    public static UserReviewSearchResponseDto from(Review review) {
        return new UserReviewSearchResponseDto(
                review.getReviewUuid(),
                review.getStore().getStoreUuid(),
                review.getStore().getStoreName(),
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
