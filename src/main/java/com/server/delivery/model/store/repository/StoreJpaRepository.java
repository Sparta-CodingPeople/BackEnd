package com.server.delivery.model.store.repository;

import com.server.delivery.model.store.entity.Store;
import com.server.delivery.model.store.entity.StoreCategory;
import com.server.delivery.model.store.entity.StoreOperationTimes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StoreJpaRepository extends JpaRepository<Store, UUID> {

    Optional<StoreCategory> findCategoryById(int storeCategoryId);

}
