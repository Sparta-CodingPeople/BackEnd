package com.server.delivery.model.store.repository;

import com.server.delivery.model.store.entity.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepository {
    private final StoreJpaRepository storeJpaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Store saveStore(Store store) {
        storeJpaRepository.save(store);
        return store;
    }

    @Override
    public Optional<Store> findById(UUID storeUuid) {
        return storeJpaRepository.findById(storeUuid);
    }


    @Override
    public Optional<SeoulAreaCode> findBySeoulRegionCode(int seoulRegionCode) {
        return storeJpaRepository.findBySeoulRegionCode(seoulRegionCode);
    }

    @Override
    public Optional<Location> findLocationBySeoulRegionCode(int seoulRegionCode) {
        return storeJpaRepository.findLocationBySeoulRegionCode(seoulRegionCode);
    }


    @Override
    public Optional<StoreLocation> findStoreLocationByLocation(Location location) {
        return storeJpaRepository.findStoreLocationByLocation(location);
    }

    @Override
    public void saveOperationTimes(OperationTimes operationTimes) {

    }

    @Override
    public void saveStoreOperationTimes(StoreOperationTimes storeOperationTimes) {

    }

    @Override
    public Optional<StoreCategory> findByStoreCategoryId(int storeCategoryId) {
        return storeJpaRepository.findStoreCategoryById(storeCategoryId);
    }


}
