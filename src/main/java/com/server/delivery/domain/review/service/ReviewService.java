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
import com.server.delivery.domain.review.dto.req.ReviewCreateRequestDto;
import com.server.delivery.domain.review.dto.req.ReviewUpdateRequestDto;
import com.server.delivery.domain.review.dto.res.ReviewCreateResponseDto;
import com.server.delivery.domain.review.dto.res.ReviewDetailSearchResponseDto;
import com.server.delivery.domain.review.dto.res.ReviewUpdateResponseDto;
import com.server.delivery.domain.review.dto.res.StoreReviewSearchResponseDto;
import com.server.delivery.domain.review.dto.res.UserReviewSearchResponseDto;
import com.server.delivery.domain.review.repository.ReviewImageJpaRepository;
import com.server.delivery.domain.review.repository.ReviewJpaRepository;
import com.server.delivery.model.review.entity.Review;
import com.server.delivery.model.review.entity.ReviewImage;
import com.server.delivery.model.user.entity.User;
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
	//private final OrderJpaRepository orderJpaRepository;
	private final ReviewJpaRepository reviewJpaRepository;
	private final ReviewImageJpaRepository reviewImageJpaRepository;

	@Transactional
	public ReviewCreateResponseDto createReview(
		Long userId,
		ReviewCreateRequestDto request,
		List<MultipartFile> images
	) {
		// todo. Order api 구현 완료 후 추가
		// 주문 정보 가져오기
		// Order foundOrder = orderJpaRepository.findById(request.orderId())
		// 	.orElseThrow(() -> new CustomOrderException(ExceptionCode.ORDER_NOT_FOUND));

		// todo. Store api 구현 완료 후 추가
		// 가게 정보 가져오기
		//Store store = foundOrder.getStore();

		// 유저 가져오기
		User user = userHelper.getUserById(userId);

		Review review = Review.builder()
			.user(user)
			//.order(foundOrder)
			//.store(store) todo
			.content(request.content())
			.rating(request.rating())
			.build();

		// 리뷰 저장
		reviewJpaRepository.save(review);

		// 이미지 처리 (imageService를 사용하여 저장)
		// 이미지가 없는 경우 pass
		if (images != null && !images.isEmpty()) {
			List<ReviewImage> reviewImages = processReviewImages(review, images);
			// ReviewImage 저장
			reviewImageJpaRepository.saveAll(reviewImages);
		}

		// 응답 반환
		return ReviewCreateResponseDto.from(review.getId());
	}

	@Transactional
	public ReviewUpdateResponseDto updateReview(
		UUID reviewId,
		ReviewUpdateRequestDto request,
		List<MultipartFile> images
	) {
		// 리뷰 조회
		Review foundReview = reviewJpaRepository.findById(reviewId)
			.orElseThrow(() -> new CustomReviewException(ExceptionCode.REVIEW_NOT_FOUND));

		// ref. 3일 이내 수정 가능
		if (LocalDateTime.now().isAfter(foundReview.getCreatedAt().plusDays(3))) {
			throw new CustomReviewException(ExceptionCode.REVIEW_UPDATE_EXPIRED);
		}

		// 리뷰 업데이트 및 변경된 리뷰 저장
		foundReview.updateReview(request.content(), request.rating());
		reviewJpaRepository.save(foundReview);

		// 이미지 처리 (기존 이미지 삭제 후 새로운 이미지 저장)
		List<ReviewImage> foundReviewImages = foundReview.getImages();
		if (foundReviewImages != null && !foundReviewImages.isEmpty()) {
			for (ReviewImage foundReviewImage : foundReviewImages) {
				s3ImageUtil.deleteImageFromS3(foundReviewImage.getImageUrl());
			}
			reviewImageJpaRepository.deleteAll(foundReviewImages);
		}

		if (images != null && !images.isEmpty()) {
			List<ReviewImage> newReviewImages = processReviewImages(foundReview, images);
			reviewImageJpaRepository.saveAll(newReviewImages);

			// 리뷰에 새로운 이미지 추가
			foundReview.updateReviewImages(newReviewImages);
			reviewJpaRepository.save(foundReview);
		}

		// 응답 반환
		return ReviewUpdateResponseDto.from(foundReview);
	}

	@Transactional(readOnly = true)
	public ReviewDetailSearchResponseDto searchReview(UUID reviewId) {
		// 리뷰 조회 (reviewId)
		Review foundReview = reviewJpaRepository.findById(reviewId)
			.orElseThrow(() -> new CustomReviewException(ExceptionCode.REVIEW_NOT_FOUND));

		// 응답 반환
		return ReviewDetailSearchResponseDto.from(foundReview);
	}

	// todo. 테스트 필요
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
		// 리뷰 조회 (userId로 작성자 확인)
		Review review = reviewJpaRepository.findById(reviewId)
			.orElseThrow(() -> new CustomReviewException(ExceptionCode.REVIEW_NOT_FOUND));

		// 이미지 삭제
		reviewImageJpaRepository.deleteAll(review.getImages());
		// 리뷰 삭제
		reviewJpaRepository.delete(review);
	}

	private List<ReviewImage> processReviewImages(Review review, List<MultipartFile> images) {
		List<ReviewImage> reviewImages = new ArrayList<>();
		for (MultipartFile image : images) {
			try {
				String imageUrl = s3ImageUtil.uploadImageToS3(image);
				log.trace("review images successfully changed ");
				// todo.
				//String originalFilename = image.getOriginalFilename();
				//Long size = image.getSize();

				ReviewImage reviewImage = ReviewImage.builder()
					.review(review)
					.imageUrl(imageUrl)
					//.imageName(originalFilename)
					//.imageSize(size)
					.build();

				reviewImages.add(reviewImage);
			} catch (Exception e) {
				log.error("이미지 업로드 실패: {}", e.getMessage());
			}
		}
		return reviewImages;
	}
}
