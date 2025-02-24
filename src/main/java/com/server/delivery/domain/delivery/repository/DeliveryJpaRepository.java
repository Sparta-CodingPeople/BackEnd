package com.server.delivery.domain.delivery.repository;

import com.server.delivery.model.delivery.entity.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface DeliveryJpaRepository extends JpaRepository<Delivery, UUID> {
    @Query("select d from Delivery d "
            + "join fetch d.order o "
            + "join fetch o.orderMenus om "
            + "where (:keyword is null "
            + "or o.deliveryAddress like %:keyword% "
            + "or om.menu.menuName like %:keyword%)")
    Page<Delivery> searchDeliveries(String keyword, Pageable validatedPageable);
}
