package com.server.delivery.util.helper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.server.delivery.common.exception.CustomReviewException;
import com.server.delivery.common.exception.ExceptionCode;
import com.server.delivery.domain.review.repository.ReviewJpaRepository;
import com.server.delivery.model.review.entity.Review;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReviewHelper {
	private final ReviewJpaRepository reviewJpaRepository;

	public Review getReview(UUID reviewId) {
		return reviewJpaRepository.findById(reviewId)
			.orElseThrow(() -> new CustomReviewException(ExceptionCode.REVIEW_NOT_FOUND));
	}

	public Review saveReview(Review review) {
		return reviewJpaRepository.save(review);
	}
}
