package com.server.delivery.model.store.repository.storeCategoryMapping;

import com.server.delivery.model.store.entity.StoreCategoryMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StoreCategoryMapingJpaRepository extends JpaRepository<StoreCategoryMapping, UUID> {
}
