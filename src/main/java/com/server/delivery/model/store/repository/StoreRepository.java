package com.server.delivery.model.store.repository;

import com.server.delivery.model.store.entity.Store;
import com.server.delivery.model.store.entity.StoreCategory;
import com.server.delivery.model.store.entity.StoreOperationTimes;

import java.util.Optional;

public interface StoreRepository {
    Store saveStore(Store store);

    Optional<StoreCategory> findCategoryById(int storeCategoryId);

    void saveStoreOperationTimes(StoreOperationTimes storeOperationTimes);

}
