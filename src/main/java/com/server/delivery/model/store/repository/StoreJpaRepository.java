package com.server.delivery.model.store.repository;

import com.server.delivery.model.store.entity.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreJpaRepository extends JpaRepository<Store, UUID> {

    Optional<Store> findById(UUID id);

    @Query("SELECT sc FROM StoreCategory sc WHERE sc.id = :id")
    Optional<StoreCategory> findStoreCategoryById(@Param("id") int id);

    @Query("SELECT sac FROM SeoulAreaCode sac WHERE sac.seoulRegionCode = :seoulRegionCode")
    Optional<SeoulAreaCode> findBySeoulRegionCode(@Param("seoulRegionCode") int seoulRegionCode);



    Optional<Store> findByStoreUuidAndStoreIsDeletedFalse(UUID storeUuid);



    @Query("SELECT l FROM Location l WHERE l.address = :address AND l.locationCategory.seoulRegionCode = :categoryId")
    Optional<Location> findLocationByAddressAndCategory(@Param("address") String address, @Param("categoryId") Long categoryId);

    @Query("SELECT sl FROM StoreLocation sl WHERE sl.location.locationUuid = :locationUuid")
    Optional<StoreLocation> findByLocationUuid(@Param("locationUuid") UUID locationUuid);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO p_operation_times " +
            "(store_operating_times_uuid, weekday, operation_times_opening_time, operation_times_closing_time, operation_times_is_holiday) " +
            "VALUES (:uuid,CAST(:weekday AS integer[]), :operationTimeOpeningTime, :operationTimeClosingTime, :operationTimeIsHoliday)",
            nativeQuery = true)
    void insertOperationTimes(@Param("uuid") UUID uuid,
                              @Param("weekday") String weekday,
                              @Param("operationTimeOpeningTime") String operationTimeOpeningTime,
                              @Param("operationTimeClosingTime") String operationTimeClosingTime,
                              @Param("operationTimeIsHoliday") boolean isHoliday);



    @Modifying
    @Transactional
    @Query(value = "INSERT INTO p_store_operation_times (store_operation_times_uuid, store_uuid, operation_times_uuid) " +
            "VALUES (:storeOperationTimesUuid, :storeUuid, :operationTimesUuid)", nativeQuery = true)
    void insertStoreOperationTimes(
            @Param("storeOperationTimesUuid") UUID storeOperationTimesUuid,
            @Param("storeUuid") UUID storeUuid,
            @Param("operationTimesUuid") UUID operationTimesUuid
    );



    @Query(value = "SELECT * FROM p_operation_times ot " +
            "WHERE ot.operation_times_opening_time = :operationTimeOpeningTime " +
            "AND ot.operation_times_closing_time = :operationTimeClosingTime " +
            "AND ot.operation_times_is_holiday = :isHoliday " +
            "AND ot.weekday && string_to_array(:weekdays, ',')::INTEGER[]",
            nativeQuery = true)
    List<OperationTimes> findOperationTimes(
            @Param("weekdays") String weekdays,
            @Param("operationTimeOpeningTime") String operationTimeOpeningTime,
            @Param("operationTimeClosingTime") String operationTimeClosingTime,
            @Param("isHoliday") boolean isHoliday);


    @Modifying
    @Transactional
    @Query(value = "UPDATE p_store_operation_times " +
            "SET operation_times_uuid = :operationTimesUuid " +
            "WHERE store_uuid = :storeUuid", nativeQuery = true)
    void updateStoreOperationTimes(@Param("storeUuid") UUID storeUuid,
                                   @Param("operationTimesUuid") UUID operationTimesUuid);

    @Modifying
    @Transactional
    @Query(value = "UPDATE p_operation_times  SET weekday = CAST(:weekday AS integer[]),  " +
            "operation_times_opening_time = :operationTimeOpeningTime, " +
            "operation_times_closing_time = :operationTimeClosingTime, " +
            "operation_times_is_holiday = :isHoliday " +
            "WHERE store_operating_times_uuid = :operationTimesUuid",nativeQuery = true)
    void updateOperationTimes(@Param("operationTimesUuid") UUID operationTimesUuid,
                              @Param("weekday") String weekday,
                              @Param("operationTimeOpeningTime") String operationTimeOpeningTime,
                              @Param("operationTimeClosingTime") String operationTimeClosingTime,
                              @Param("isHoliday") boolean isHoliday);


    @Query(value = "SELECT sot.operation_times_uuid FROM p_store_operation_times sot " +
            "JOIN p_operation_times ot ON sot.operation_times_uuid = ot.store_operating_times_uuid " +
            "WHERE sot.store_uuid = :storeUuid " +
            "AND ot.weekday = CAST(:weekday AS INTEGER[]) " +
            "AND ot.operation_times_opening_time = :operationTimeOpeningTime " +
            "AND ot.operation_times_closing_time = :operationTimeClosingTime " +
            "AND ot.operation_times_is_holiday = :storeIsClosed", nativeQuery = true)
    Optional<UUID> findMatchingOperationTimesUuid(@Param("storeUuid") UUID storeUuid,
                                                  @Param("weekday") String weekday,
                                                  @Param("operationTimeOpeningTime") String operationTimeOpeningTime,
                                                  @Param("operationTimeClosingTime") String operationTimeClosingTime,
                                                  @Param("storeIsClosed") boolean storeIsClosed);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM p_operation_times WHERE store_operating_times_uuid = :operationTimesUuid)",
            nativeQuery = true)
    boolean existsByOperationTimesUuid(UUID operationTimesUuid);

    @Query("SELECT s FROM Store s " +
            "WHERE (:search IS NULL OR LOWER(s.storeName) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "ORDER BY s.createdAt DESC")
    Page<Store> searchStores(@Param("search") String search, Pageable pageable);

    @Query("SELECT COUNT(s) FROM Store s " +
            "WHERE (:search IS NULL OR LOWER(s.storeName) LIKE LOWER(CONCAT('%', :search, '%')))")
    Long countStores(@Param("search") String search);
}

