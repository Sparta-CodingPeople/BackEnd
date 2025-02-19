package com.server.delivery.model.store.repository;

import com.server.delivery.model.store.entity.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;


public interface StoreRepository {
    Store saveStore(Store store);

    Optional<StoreCategory> findById(int storeCategoryId);


    Optional<SeoulAreaCode>  findBySeoulRegionCode(int seoulRegionCode);

    @Query("SELECT sl.location FROM StoreLocation sl WHERE sl.location.locationCategory.seoulRegionCode= :seoulRegionCode")
    Optional<Location> findLocationBySeoulRegionCode(@Param("seoulRegionCode") int seoulRegionCode);

    @Query("SELECT sl FROM StoreLocation sl WHERE sl.location = :location")
    Optional<StoreLocation> findStoreLocationByLocation(@Param("location") Location location);

    void saveOperationTimes(OperationTimes operationTimes);

    void saveStoreOperationTimes(StoreOperationTimes storeOperationTimes);
}
