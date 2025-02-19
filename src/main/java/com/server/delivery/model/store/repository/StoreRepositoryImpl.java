package com.server.delivery.model.store.repository;

import com.server.delivery.model.store.entity.Store;
import com.server.delivery.model.store.entity.StoreCategory;

import com.server.delivery.model.store.entity.StoreOperationTimes;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepository {
    private final StoreJpaRepository storeJpaRepository;
    private final EntityManager entityManager;

    @Override
    public Store saveStore(Store store) {
        storeJpaRepository.save(store);
        return store;
    }

    @Override
    public Optional<StoreCategory> findCategoryById(int storeCategoryId) {
        return storeJpaRepository.findCategoryById(storeCategoryId);
    }

    public void saveStoreOperationTimes(StoreOperationTimes storeOperationTimes) {
        entityManager.persist(storeOperationTimes);
    }


}
