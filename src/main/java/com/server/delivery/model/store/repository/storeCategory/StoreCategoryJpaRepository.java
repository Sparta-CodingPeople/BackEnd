package com.server.delivery.model.store.repository.storeCategory;

import com.server.delivery.model.store.entity.StoreCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StoreCategoryJpaRepository extends JpaRepository<StoreCategory, UUID> {
}
