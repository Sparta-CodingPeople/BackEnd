package com.server.delivery.domain.review.controller;

import java.util.List;

import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.server.delivery.common.jwt.CustomUserDetail;
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
		@AuthenticationPrincipal CustomUserDetail userDetail,
		@RequestPart("review") ReviewCreateRequestDto request,
		@RequestPart(value = "images", required = false) List<MultipartFile> images
	) {
		ReviewCreateResponseDto response = reviewService.createReview(userDetail.getUserId(), request, images);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// todo. 업데이트 날짜 추가 (ex. 3일 이내 수정 가능)
	@PatchMapping("/v1/reviews/{reviewId}")
	public ResponseEntity<ReviewUpdateResponseDto> updateReview(
		@AuthenticationPrincipal CustomUserDetail userDetail,
		@PathVariable Long reviewId,
		@RequestPart("review") ReviewUpdateRequestDto request,
		@RequestPart(value = "images", required = false) List<MultipartFile> images
	) {
		ReviewUpdateResponseDto response = reviewService.updateReview(userDetail.getUserId(), reviewId, request,
			images);
		return ResponseEntity.ok(response);
	}

	// 특정 리뷰 1건의 상세 내용이 필요한 경우
	@GetMapping("/v1/reviews/{reviewId}")
	public ResponseEntity<ReviewDetailSearchResponseDto> searchReview(@PathVariable Long reviewId) {
		ReviewDetailSearchResponseDto response = reviewService.searchReview(reviewId);
		return ResponseEntity.ok(response);
	}

	// 특정 음식점의 리뷰 목록을 확인할 경우
	@GetMapping("/v1/reviews/stores/{storeId}")
	public ResponseEntity<PagedModel<StoreReviewSearchResponseDto>> searchStoreReviews(
		@PathVariable Long storeId,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		PagedModel<StoreReviewSearchResponseDto> reviewPage = reviewService.searchStoreReviews(storeId, page, size);
		return ResponseEntity.ok(reviewPage);
	}

	// 특정 사용자가 작성한 리뷰 목록을 확인할 경우 == 본인 리뷰 조회
	//@GetMapping("/v1/reviews/me")
	@GetMapping("/v1/users/{userId}/reviews")
	public ResponseEntity<PagedModel<UserReviewSearchResponseDto>> searchUserReviews(
		@AuthenticationPrincipal CustomUserDetail userDetail,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		PagedModel<UserReviewSearchResponseDto> reviewPage = reviewService.searchUserReviews(userDetail.getUserId(),
			page, size);
		return ResponseEntity.ok(reviewPage);
	}

	@DeleteMapping("/v1/reviews/{reviewId}")
	public ResponseEntity<Void> deleteReview(
		@AuthenticationPrincipal CustomUserDetail userDetail,
		@PathVariable Long reviewId
	) {
		reviewService.deleteReview(userDetail.getUserId(), reviewId);
		return ResponseEntity.noContent().build();
	}
}
