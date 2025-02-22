package com.server.delivery.model.store.repository.store;

import com.server.delivery.model.store.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepository {
    private final StoreJpaRepository storeJpaRepository;

    @Override
    public Optional<Store> findByStoreUuid(UUID storeUuid) {
        return storeJpaRepository.findByStoreUuid(storeUuid);
    }


    @Override
    public Store save(Store store) {
        return storeJpaRepository.save(store);
    }

    @Override
    public boolean existsStoreByStoreName(String storeName) {
        return storeJpaRepository.existsStoreByStoreName(storeName);
    }

    @Override
    public Page<Store> findByStoreNameContainingAndStoreIsGrantedTrue(String keyword, Pageable sortedPageable) {
        return storeJpaRepository.findByStoreNameContainingAndStoreIsGrantedTrue(keyword, sortedPageable);

    }

    @Override
    public Page<Store> findByStoreIsGrantedFalse(Pageable sortedPageable) {

        return storeJpaRepository.findByStoreIsGrantedFalse(sortedPageable);
    }

    @Override
    public Store findStoreWithReviews(UUID storeUuid) {

        return storeJpaRepository.findStoreWithReviews(storeUuid);
    }
}
