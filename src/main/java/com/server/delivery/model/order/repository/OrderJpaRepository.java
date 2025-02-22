package com.server.delivery.model.order.repository;

import com.server.delivery.model.order.entity.Order;
import com.server.delivery.model.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderJpaRepository extends JpaRepository<Order, UUID> {

    Optional<Order> findByOrderUuid(UUID orderUuid);

    @Query("SELECT o FROM Order o WHERE o.store.storeName LIKE %:keyword% AND o.user = :user")
    Page<Order> findByUserAndStoreNameContaining(@Param("user") User user, @Param("keyword") String keyword, Pageable pageable);
}
