package com.server.delivery.model.store.repository;

import com.server.delivery.model.store.entity.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface StoreRepository {


    Optional<Store> findById(UUID storeUuid);


    Optional<SeoulAreaCode> findBySeoulRegionCode(int seoulRegionCode);


    Optional<StoreCategory> findByStoreCategoryId(int storeCategoryId);


    Store save(Store store);

    Optional<Store> findByStoreUuidAndStoreIsDeletedFalse(UUID storeUuid);


    Optional<Location> findLocationByAddressAndCategory(String address, int seoulRegionCode);


    Optional<StoreLocation> findByLocationUuid(UUID locationUuid);


    void insertOperationTimes(UUID uuid, String weekday,  String operationTimeOpeningTime, String operationTimeClosingTime, boolean isHoliday);



    void insertStoreOperationTimes(UUID storeOperationTimesUuid, UUID storeUuid, UUID operationTimesUuid);



    List<OperationTimes> findOperationTimes(String weekdays, String operationTimeOpeningTime, String operationTimeClosingTime, boolean isHoliday);


    void updateOperationTimes(UUID storeOperatingTimesUuid, String weekdayStr, String operationTimeOpeningTime, String operationTimeClosingTime, boolean isHoliday);

    void updateStoreOperationTimes(UUID storeUuid, UUID storeOperatingTimesUuid);

    Optional<UUID> findMatchingOperationTimesUuid(UUID storeUuid, String weekday, String operationTimeOpeningTime, String operationTimeClosingTime, boolean storeIsClosed);

    boolean existsByOperationTimesUuid(UUID operationTimesUuid);

    Page<Store> searchStores(String search, Pageable pageable);

    Long countStores(String search);
}

