package com.server.delivery.domain.review.repository;

import com.server.delivery.model.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ReviewJpaRepository extends JpaRepository<Review, UUID> {
    @Query("select r from Review r "
            + "join fetch r.store "
            + "where r.store.storeUuid = :storeId "
            + "and (:keyword is null or r.content like %:keyword%)")
    Page<Review> searchByStoreReview(UUID storeId, String keyword, Pageable pageable);

    @Query("select r from Review r "
            + "join fetch r.user "
            + "where r.user.id = :userId "
            + "and (:keyword is null or r.content like %:keyword%)")
    Page<Review> searchUserReviews(Long userId, String keyword, Pageable pageable);
}
