package com.server.delivery.model.store.repository.storeOperationTimes;

import com.server.delivery.model.store.entity.StoreOperationTimes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StoreOperationTimesJpaRepository extends JpaRepository<StoreOperationTimes, UUID> {
}
