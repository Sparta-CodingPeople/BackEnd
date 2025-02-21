package com.server.delivery.model.store.repository.storeOperationTimes;

import com.server.delivery.model.store.entity.StoreOperationTimes;

import java.util.List;

public interface StoreOperationTimesRepository {
    List<StoreOperationTimes> saveAll(List<StoreOperationTimes> storeOperationTimesList);
}
