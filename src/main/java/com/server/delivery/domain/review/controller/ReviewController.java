package com.server.delivery.domain.review.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.server.delivery.common.jwt.CustomUserDetail;
import com.server.delivery.common.response.CustomResponse;
import com.server.delivery.common.response.ResponseMessage;
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

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/v1/reviews")
	public CustomResponse<ReviewCreateResponseDto> createReview(
		@AuthenticationPrincipal CustomUserDetail userDetail,
		@RequestPart("review") ReviewCreateRequestDto request,
		@RequestPart(value = "reviewImages", required = false) List<MultipartFile> images
	) {
		ReviewCreateResponseDto response = reviewService.createReview(userDetail.getUserId(), request, images);
		return CustomResponse.success(ResponseMessage.REVIEW_CREATED.getMessage(), response);
	}

	@ResponseStatus(HttpStatus.OK)
	@PatchMapping("/v1/reviews/{reviewId}")
	public CustomResponse<ReviewUpdateResponseDto> updateReview(
		@PathVariable UUID reviewId,
		@RequestPart("review") ReviewUpdateRequestDto request,
		@RequestPart(value = "reviewImages", required = false) List<MultipartFile> images
	) {
		ReviewUpdateResponseDto response = reviewService.updateReview(reviewId, request, images);
		return CustomResponse.success(ResponseMessage.REVIEW_UPDATED.getMessage(), response);
	}

	// 특정 리뷰 1건의 상세 내용이 필요한 경우
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/v1/reviews/{reviewId}")
	public CustomResponse<ReviewDetailSearchResponseDto> searchReview(@PathVariable UUID reviewId) {
		ReviewDetailSearchResponseDto response = reviewService.searchReview(reviewId);
		return CustomResponse.success(ResponseMessage.REVIEW_SEARCH.getMessage(), response);
	}

	// todo. Store api 구현 후 테스트
	// 특정 음식점의 리뷰 목록을 확인할 경우
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/v1/reviews/stores/{storeId}")
	public CustomResponse<PagedModel<StoreReviewSearchResponseDto>> searchStoreReviews(
		@PathVariable UUID storeId,
		@RequestParam(required = false) String keyword,
		@PageableDefault(sort = {"createdAt"}, direction = Sort.Direction.DESC, size = 10) Pageable pageable
	) {
		PagedModel<StoreReviewSearchResponseDto> reviewPage = reviewService.searchStoreReviews(storeId, keyword,
			pageable);
		return CustomResponse.success(ResponseMessage.STORE_REVIEW_SEARCH.getMessage(), reviewPage);
	}

	// 특정 사용자가 작성한 리뷰 목록을 확인할 경우 == 본인 리뷰 조회
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/v1/users/{userId}/reviews")
	public CustomResponse<PagedModel<UserReviewSearchResponseDto>> searchUserReviews(
		@AuthenticationPrincipal CustomUserDetail userDetail,
		@RequestParam(required = false) String keyword,
		@PageableDefault(sort = {"createdAt"}, direction = Sort.Direction.DESC, size = 10) Pageable pageable
	) {
		PagedModel<UserReviewSearchResponseDto> reviewPage = reviewService.searchUserReviews(userDetail.getUserId(),
			keyword, pageable);
		return CustomResponse.success(ResponseMessage.USER_REVIEW_SEARCH.getMessage(), reviewPage);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/v1/reviews/{reviewId}")
	public CustomResponse<Void> deleteReview(@PathVariable UUID reviewId) {
		reviewService.deleteReview(reviewId);
		return CustomResponse.success(ResponseMessage.REVIEW_DELETE.getMessage());
	}
}
