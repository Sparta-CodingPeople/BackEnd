package com.server.delivery.model.store.repository.store;

import com.server.delivery.model.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StoreJpaRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByStoreUuid(UUID storeUuid);

    boolean existsStoreByStoreName(String storeName);

    Page<Store> findByStoreNameContainingAndStoreIsGrantedTrue(String keyword, Pageable sortedPageable);
}
