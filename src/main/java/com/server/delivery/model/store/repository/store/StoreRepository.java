package com.server.delivery.model.store.repository.store;

import com.server.delivery.model.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface StoreRepository {
    Optional<Store> findByStoreUuid(UUID storeUuid);

    Store save(Store store);

    boolean existsStoreByStoreName(String storeName);

    Page<Store> findByStoreNameContainingAndStoreIsGrantedTrue(String keyword, Pageable sortedPageable);

    Page<Store> findByStoreIsGrantedFalse(Pageable sortedPageable);

    Store findStoreWithReviews(UUID storeUuid);

    Page<Store> findAllStoreIsGrantedTrue(Pageable sortedPageable);
}
