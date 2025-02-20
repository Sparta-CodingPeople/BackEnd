package com.server.delivery.model.store.repository;

import com.server.delivery.model.store.entity.*;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepository {
    private final StoreJpaRepository storeJpaRepository;


    @Override
    public Optional<Store> findById(UUID storeUuid) {
        return storeJpaRepository.findById(storeUuid);
    }


    @Override
    public Optional<SeoulAreaCode> findBySeoulRegionCode(int seoulRegionCode) {
        return storeJpaRepository.findBySeoulRegionCode(seoulRegionCode);
    }




    @Override
    public Optional<StoreCategory> findByStoreCategoryId(int storeCategoryId) {
        return storeJpaRepository.findStoreCategoryById(storeCategoryId);
    }

    @Override
    public Store save(Store store) {
        return storeJpaRepository.save(store);
    }

    @Override
    public Optional<Store> findByStoreUuidAndStoreIsDeletedFalse(UUID storeUuid) {
        return storeJpaRepository.findByStoreUuidAndStoreIsDeletedFalse(storeUuid);
    }


    @Override
    public Optional<Location> findLocationByAddressAndCategory(String address, int seoulRegionCode) {
        return storeJpaRepository.findLocationByAddressAndCategory(address, (long) seoulRegionCode);
    }

    @Override
    public Optional<StoreLocation> findByLocationUuid(UUID locationUuid) {
        return storeJpaRepository.findByLocationUuid(locationUuid);
    }

    @Override
    public void insertOperationTimes(UUID uuid, String weekday, String operationTimeOpeningTime, String operationTimeClosingTime, boolean isHoliday) {
        storeJpaRepository.insertOperationTimes(uuid, weekday, operationTimeOpeningTime, operationTimeClosingTime, isHoliday);
    }


    @Override
    public void insertStoreOperationTimes(UUID storeOperationTimesUuid, UUID storeUuid, UUID operationTimesUuid) {
        storeJpaRepository.insertStoreOperationTimes(storeOperationTimesUuid, storeUuid, operationTimesUuid);
    }

    @Override
    public List<OperationTimes> findOperationTimes(String weekdays, String operationTimeOpeningTime, String operationTimeClosingTime, boolean isHoliday) {
        return storeJpaRepository.findOperationTimes(weekdays, operationTimeOpeningTime, operationTimeClosingTime, isHoliday);
    }

    @Override
    public void updateOperationTimes(UUID storeOperatingTimesUuid, String weekdayStr, String operationTimeOpeningTime, String operationTimeClosingTime, boolean isHoliday) {
        storeJpaRepository.updateOperationTimes(storeOperatingTimesUuid, weekdayStr, operationTimeOpeningTime, operationTimeClosingTime, isHoliday);
    }

    @Override
    public void updateStoreOperationTimes(UUID storeUuid, UUID storeOperatingTimesUuid) {
        storeJpaRepository.updateStoreOperationTimes(storeUuid, storeOperatingTimesUuid);
    }

    @Override
    public Optional<UUID> findMatchingOperationTimesUuid(UUID storeUuid, String weekday, String operationTimeOpeningTime, String operationTimeClosingTime, boolean isHoliday) {
        return storeJpaRepository.findMatchingOperationTimesUuid(storeUuid, weekday, operationTimeOpeningTime, operationTimeClosingTime, isHoliday);
    }

    @Override
    public boolean existsByOperationTimesUuid(UUID operationTimesUuid) {
        return storeJpaRepository.existsByOperationTimesUuid(operationTimesUuid);
    }

    @Override
    public Page<Store> searchStores(String search, Pageable pageable) {
        return storeJpaRepository.searchStores(search, pageable);
    }

    @Override
    public Long countStores(String search) {
        return storeJpaRepository.countStores(search);
    }


}
