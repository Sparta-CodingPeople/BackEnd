package com.server.delivery.domain.payment.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.server.delivery.model.payment.Payment;

public interface PaymentJpaRepository extends JpaRepository<Payment, UUID> {
	@Query("select exists (select 1 from Payment p where p.order.id = :serverOrderId)")
	boolean existsByOrderUuid(UUID serverOrderId);

	@Query("select p from Payment p "
		+ "join fetch p.user "
		+ "where p.user.id = :userId "
		+ "order by p.order.createdAt desc")
	Page<Payment> searchUserReviews(Long userId, Pageable pageable);
}
