package com.server.delivery.model.store.repository.ownerStore;

import com.server.delivery.model.owner.entity.OwnerStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OwnerStoreRepositoryImpl implements OwnerStoreRepository {
    private final OwnerStoreJpaRepository ownerStoreJpaRepository;

    @Override
    public OwnerStore save(OwnerStore ownerStore) {
        return ownerStoreJpaRepository.save(ownerStore);
    }
}
