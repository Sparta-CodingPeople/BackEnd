package com.server.delivery.model.store.repository;

import com.server.delivery.model.store.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepository {
    private final StoreJpaRepository storeRepository;

    @Override
    public Optional<Store> findByStoreUuid(UUID storeUuid) {
        return storeRepository.findByStoreUuid(storeUuid);
    }
}
