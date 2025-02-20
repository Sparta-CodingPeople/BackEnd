package com.server.delivery.domain.review.repository;

import com.server.delivery.model.review.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewImageJpaRepository extends JpaRepository<ReviewImage, UUID> {
}
