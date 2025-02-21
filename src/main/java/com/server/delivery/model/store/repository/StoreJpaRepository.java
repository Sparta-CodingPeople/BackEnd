package com.server.delivery.model.store.repository;

import com.server.delivery.model.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StoreJpaRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByStoreUuid(UUID storeUuid);
}
