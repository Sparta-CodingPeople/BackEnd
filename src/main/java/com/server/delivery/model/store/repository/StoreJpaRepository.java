package com.server.delivery.model.store.repository;

import com.server.delivery.model.store.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface StoreJpaRepository extends JpaRepository<Store, UUID> {

    Optional<Store> findById(UUID id);

    @Query("SELECT sc FROM StoreCategory sc WHERE sc.id = :id")
    Optional<StoreCategory> findStoreCategoryById(@Param("id") int id);

    @Query("SELECT sac FROM SeoulAreaCode sac WHERE sac.seoulRegionCode = :seoulRegionCode")
    Optional<SeoulAreaCode> findBySeoulRegionCode(@Param("seoulRegionCode") int seoulRegionCode);


    @Query("SELECT sl.location FROM StoreLocation sl WHERE sl.location.locationCategory.seoulRegionCode = :seoulRegionCode")
    Optional<Location> findLocationBySeoulRegionCode(@Param("seoulRegionCode") int seoulRegionCode);

    @Query("SELECT sl FROM StoreLocation sl WHERE sl.location = :location")
    Optional<StoreLocation> findStoreLocationByLocation(@Param("location") Location location);

}
