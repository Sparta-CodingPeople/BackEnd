package com.server.delivery.model.store.repository.store;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.server.delivery.model.store.entity.Store;

public interface StoreJpaRepository extends JpaRepository<Store, UUID> {
	Optional<Store> findByStoreUuid(UUID storeUuid);

	boolean existsStoreByStoreName(String storeName);

	Page<Store> findByStoreNameContainingAndStoreIsGrantedTrue(String keyword, Pageable sortedPageable);
}
