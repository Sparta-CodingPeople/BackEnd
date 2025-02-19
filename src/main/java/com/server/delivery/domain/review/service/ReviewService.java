package com.server.delivery.domain.review.service;

import com.server.delivery.domain.review.dto.req.ReviewCreateRequestDto;
import com.server.delivery.domain.review.dto.req.ReviewUpdateRequestDto;
import com.server.delivery.domain.review.dto.res.*;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ReviewService {
    public ReviewCreateResponseDto createReview(
            Long userId,
            ReviewCreateRequestDto request,
            List<MultipartFile> images
    ) {
        return null;
    }

    public ReviewDetailSearchResponseDto searchReview(Long reviewId) {
        return null;
    }

    public ReviewUpdateResponseDto updateReview(
            Long userId,
            Long reviewId,
            ReviewUpdateRequestDto request,
            List<MultipartFile> images
    ) {
        return null;
    }

    public PagedModel<StoreReviewSearchResponseDto> searchStoreReviews(
            Long restaurantId,
            int page,
            int size
    ) {
        return null;
    }

    public PagedModel<UserReviewSearchResponseDto> searchUserReviews(
            Long userId,
            int page,
            int size
    ) {
        return null;
    }

    public void deleteReview(
            Long userId,
            Long reviewId
    ) {

    }
}
