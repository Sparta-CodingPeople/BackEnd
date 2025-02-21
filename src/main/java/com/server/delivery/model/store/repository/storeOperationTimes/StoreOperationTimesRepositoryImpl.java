package com.server.delivery.model.store.repository.storeOperationTimes;

import com.server.delivery.model.store.entity.StoreOperationTimes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class StoreOperationTimesRepositoryImpl implements StoreOperationTimesRepository {
    private final StoreOperationTimesJpaRepository storeOperationTimesJpaRepository;

    @Override
    public List<StoreOperationTimes> saveAll(List<StoreOperationTimes> storeOperationTimesList) {
        return storeOperationTimesJpaRepository.saveAll(storeOperationTimesList);
    }
}
