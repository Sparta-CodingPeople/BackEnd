package com.server.delivery.domain.review.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.server.delivery.domain.review.dto.req.ReviewCreateRequestDto;
import com.server.delivery.domain.review.dto.req.ReviewUpdateRequestDto;
import com.server.delivery.domain.review.dto.res.ReviewCreateResponseDto;
import com.server.delivery.domain.review.dto.res.ReviewDetailSearchResponseDto;
import com.server.delivery.domain.review.dto.res.ReviewUpdateResponseDto;
import com.server.delivery.domain.review.dto.res.StoreReviewSearchResponseDto;
import com.server.delivery.domain.review.dto.res.UserReviewSearchResponseDto;
import com.server.delivery.domain.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService reviewService;

	@PostMapping("/v1/reviews")
	public ResponseEntity<ReviewCreateResponseDto> createReview(
		@RequestPart("review") ReviewCreateRequestDto request,
		@RequestPart(value = "images", required = false) List<MultipartFile> images
	) {
		ReviewCreateResponseDto response = reviewService.createReview(request, images);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// memo. 업데이트 날짜 추가 (ex. 3일 이내 수정 가능)
	@PatchMapping("/v1/reviews/{reviewId}")
	public ResponseEntity<ReviewUpdateResponseDto> updateReview(
		@PathVariable Long reviewId,
		@RequestPart("review") ReviewUpdateRequestDto request,
		@RequestPart(value = "images", required = false) List<MultipartFile> images
	) {
		ReviewUpdateResponseDto response = reviewService.updateReview(reviewId, request, images);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// 특정 리뷰 1건의 상세 내용이 필요한 경우
	@GetMapping("/v1/reviews/{reviewId}")
	public ResponseEntity<ReviewDetailSearchResponseDto> searchReview(@PathVariable Long reviewId) {
		ReviewDetailSearchResponseDto response = reviewService.searchReview(reviewId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// 특정 음식점의 리뷰 목록을 확인할 경우
	@GetMapping("/v1/reviews/store/{storeId}")
	public ResponseEntity<List<StoreReviewSearchResponseDto>> searchStoreReviews(@PathVariable Long storeId) {
		List<StoreReviewSearchResponseDto> response = reviewService.searchStoreReviews(storeId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// 특정 사용자가 작성한 리뷰 목록을 확인할 경우
	@GetMapping("/v1/reviews/store/{userId}")
	public ResponseEntity<List<UserReviewSearchResponseDto>> searchUserReviews(@PathVariable Long userId) {
		List<UserReviewSearchResponseDto> response = reviewService.searchUserReviews(userId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/v1/reviews/{reviewId}")
	public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
		reviewService.deleteReview(reviewId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
