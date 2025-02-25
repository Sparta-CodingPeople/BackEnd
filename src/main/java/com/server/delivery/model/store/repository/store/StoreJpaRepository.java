package com.server.delivery.model.store.repository.store;

import com.server.delivery.model.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface StoreJpaRepository extends JpaRepository<Store, UUID> {
    Optional<Store> findByStoreUuid(UUID storeUuid);

    boolean existsStoreByStoreName(String storeName);

    Page<Store> findByStoreNameContainingAndStoreIsGrantedTrue(String keyword, Pageable sortedPageable);

    Page<Store> findByStoreIsGrantedFalse(Pageable sortedPageable);

    @Query("SELECT s FROM Store s LEFT JOIN FETCH s.reviews WHERE s.storeUuid = :storeUuid")
    Store findStoreWithReviews(UUID storeUuid);

    Page<Store> findAllByStoreIsGrantedTrue(Pageable sortedPageable);
}
