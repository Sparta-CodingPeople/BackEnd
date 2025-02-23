package com.server.delivery.domain.review.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.server.delivery.common.exception.CustomReviewException;
import com.server.delivery.common.exception.ExceptionCode;
import com.server.delivery.common.exception.customException.CustomOrderException;
import com.server.delivery.common.exception.customException.CustomStoreException;
import com.server.delivery.domain.review.dto.req.ReviewCreateRequestDto;
import com.server.delivery.domain.review.dto.req.ReviewUpdateRequestDto;
import com.server.delivery.domain.review.dto.res.ReviewCreateResponseDto;
import com.server.delivery.domain.review.dto.res.ReviewDetailSearchResponseDto;
import com.server.delivery.domain.review.dto.res.ReviewUpdateResponseDto;
import com.server.delivery.domain.review.dto.res.StoreReviewSearchResponseDto;
import com.server.delivery.domain.review.dto.res.UserReviewSearchResponseDto;
import com.server.delivery.domain.review.repository.ReviewJpaRepository;
import com.server.delivery.model.order.entity.Order;
import com.server.delivery.model.order.repository.OrderJpaRepository;
import com.server.delivery.model.review.entity.Review;
import com.server.delivery.model.review.entity.ReviewImage;
import com.server.delivery.model.store.entity.Store;
import com.server.delivery.model.store.repository.store.StoreJpaRepository;
import com.server.delivery.model.user.entity.User;
import com.server.delivery.util.helper.ReviewHelper;
import com.server.delivery.util.helper.ReviewImageHelper;
import com.server.delivery.util.helper.UserHelper;
import com.server.delivery.util.s3image.S3ImageUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
	private final UserHelper userHelper;
	private final S3ImageUtil s3ImageUtil;
	private final OrderJpaRepository orderJpaRepository;
	private final ReviewJpaRepository reviewJpaRepository;
	private final ReviewHelper reviewHelper;
	private final ReviewImageHelper reviewImageHelper;
	private final StoreJpaRepository storeJpaRepository;

	@Transactional
	public ReviewCreateResponseDto createReview(
		Long userId,
		ReviewCreateRequestDto request,
		List<MultipartFile> images
	) {
		// 주문 정보 가져오기
		Order foundOrder = orderJpaRepository.findById(request.orderId())
			.orElseThrow(() -> new CustomOrderException(ExceptionCode.ORDER_NOT_FOUND));

		if (foundOrder.isNotDeliveryCompleted()) {
			throw new CustomReviewException(ExceptionCode.REVIEW_NOT_WRITE_DELIVERY_NOT_COMPLETED);
		}

		// 가게 정보 가져오기
		Store store = storeJpaRepository.findByStoreUuid(request.storeUuid())
			.orElseThrow(() -> new CustomStoreException(ExceptionCode.STORE_NOT_FOUND));

		// 유저 가져오기
		User user = userHelper.getUserById(userId);

		Review review = Review.builder()
			.user(user)
			.order(foundOrder)
			.store(store)
			.content(request.content())
			.rating(request.rating())
			.build();

		// 리뷰 저장
		reviewHelper.saveReview(review);

		// 이미지 처리 (이미지가 없는 경우 pass)
		if (images != null && !images.isEmpty()) {
			List<ReviewImage> reviewImages = processReviewImages(review, images);
			// ReviewImage 저장
			reviewImageHelper.saveAll(reviewImages);
		}

		// 응답 반환
		return ReviewCreateResponseDto.from(review.getReviewUuid());
	}

	@Transactional
	public ReviewUpdateResponseDto updateReview(
		UUID reviewId,
		ReviewUpdateRequestDto request,
		List<MultipartFile> images
	) {
		// 리뷰 조회
		Review foundReview = reviewHelper.getReview(reviewId);

		// 수정 가능 여부 확인
		if (LocalDateTime.now().isAfter(foundReview.getCreatedAt().plusDays(3))) {
			throw new CustomReviewException(ExceptionCode.REVIEW_UPDATE_EXPIRED);
		}

		// 리뷰 글만 수정하는 경우
		foundReview.updateReview(request.content(), request.rating());
		reviewHelper.saveReview(foundReview);

		// 리뷰 이미지를 수정하는 경우
		// 이미지 처리 (기존 이미지 삭제 후 새로운 이미지 저장)
		if (images != null && !images.isEmpty()) {
			// 기존 이미지 삭제
			List<ReviewImage> foundReviewImages = foundReview.getImages();
			if (foundReviewImages != null && !foundReviewImages.isEmpty()) {
				foundReviewImages.forEach(image -> s3ImageUtil.deleteImageFromS3(image.getImageUrl()));
			}

			// 새로운 리뷰 이미지 저장
			List<ReviewImage> newReviewImages = processReviewImages(foundReview, images);
			// dirty checking 이미지 사진 변경
			reviewImageHelper.saveAll(newReviewImages);

			// 리뷰에 새로운 이미지 추가
			foundReview.updateReviewImages(newReviewImages);
			reviewHelper.saveReview(foundReview);
		}

		// 응답 반환
		return ReviewUpdateResponseDto.from(foundReview);
	}

	@Transactional(readOnly = true)
	public ReviewDetailSearchResponseDto searchReview(UUID reviewId) {
		// 리뷰 조회 (reviewId)
		Review foundReview = reviewHelper.getReview(reviewId);

		// 응답 반환
		return ReviewDetailSearchResponseDto.from(foundReview);
	}

	@Transactional(readOnly = true)
	public PagedModel<StoreReviewSearchResponseDto> searchStoreReviews(
		UUID storeId,
		String keyword,
		Pageable pageable
	) {
		// 검색 조건 및 정렬 조건에 따라 리뷰 조회
		Page<Review> reviews = reviewJpaRepository.searchStoreReviews(storeId, keyword, pageable);
		Page<StoreReviewSearchResponseDto> content = reviews.map(StoreReviewSearchResponseDto::from);
		return new PagedModel<>(content);
	}

	@Transactional(readOnly = true)
	public PagedModel<UserReviewSearchResponseDto> searchUserReviews(
		Long userId,
		String keyword,
		Pageable pageable
	) {
		Page<Review> reviews = reviewJpaRepository.searchUserReviews(userId, keyword, pageable);
		Page<UserReviewSearchResponseDto> content = reviews.map(UserReviewSearchResponseDto::from);
		return new PagedModel<>(content);
	}

	@Transactional
	public void deleteReview(UUID reviewId) {
		Review review = reviewHelper.getReview(reviewId);

		List<ReviewImage> reviewImages = review.getImages();

		// 리뷰 이미지가 존재할 경우 소프트 삭제
		reviewImages.forEach(savedImage -> {
			s3ImageUtil.deleteImageFromS3(savedImage.getImageUrl());
			savedImage.performSoftDelete();
		});

		// 리뷰 삭제
		review.performSoftDelete();

		reviewHelper.saveReview(review);
	}

	private List<ReviewImage> processReviewImages(Review review, List<MultipartFile> images) {
		List<ReviewImage> reviewImages = new ArrayList<>();
		for (MultipartFile image : images) {
			try {
				String imageUrl = s3ImageUtil.uploadImageToS3(image);
				log.trace("review images successfully changed ");

				ReviewImage reviewImage = ReviewImage.builder()
					.review(review)
					.imageUrl(imageUrl)
					.build();

				reviewImages.add(reviewImage);
			} catch (Exception e) {
				log.error("image upload fail: {}", e.getMessage());
			}
		}
		
		reviewImageHelper.saveAll(reviewImages);

		return reviewImages;
	}
}
