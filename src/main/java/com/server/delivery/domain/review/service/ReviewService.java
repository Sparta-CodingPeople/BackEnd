package com.server.delivery.domain.review.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.server.delivery.domain.review.dto.req.ReviewCreateRequestDto;
import com.server.delivery.domain.review.dto.req.ReviewUpdateRequestDto;
import com.server.delivery.domain.review.dto.res.ReviewCreateResponseDto;
import com.server.delivery.domain.review.dto.res.ReviewDetailSearchResponseDto;
import com.server.delivery.domain.review.dto.res.ReviewUpdateResponseDto;
import com.server.delivery.domain.review.dto.res.StoreReviewSearchResponseDto;
import com.server.delivery.domain.review.dto.res.UserReviewSearchResponseDto;

@Service
public class ReviewService {
	public ReviewCreateResponseDto createReview(
		ReviewCreateRequestDto request,
		List<MultipartFile> images
	) {
		return null;
	}

	public ReviewDetailSearchResponseDto searchReview(Long reviewId) {
		return null;
	}

	public ReviewUpdateResponseDto updateReview(
		Long reviewId,
		ReviewUpdateRequestDto request,
		List<MultipartFile> images
	) {
		return null;
	}

	public List<StoreReviewSearchResponseDto> searchStoreReviews(Long restaurantId) {
		return null;
	}

	public List<UserReviewSearchResponseDto> searchUserReviews(Long userId) {
		return null;
	}

	public void deleteReview(Long reviewId) {

	}
}
