package com.server.delivery.util.helper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.server.delivery.domain.review.repository.ReviewImageJpaRepository;
import com.server.delivery.model.review.entity.ReviewImage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReviewImageHelper {
	private final ReviewImageJpaRepository reviewImageJpaRepository;

	public List<ReviewImage> saveAll(List<ReviewImage> reviewImages) {
		return reviewImageJpaRepository.saveAll(reviewImages);
	}
}
