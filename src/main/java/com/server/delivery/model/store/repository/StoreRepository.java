package com.server.delivery.model.store.repository;

import com.server.delivery.model.store.entity.Store;

import java.util.Optional;
import java.util.UUID;

public interface StoreRepository {
    Optional<Store> findByStoreUuid(UUID storeUuid);

}
