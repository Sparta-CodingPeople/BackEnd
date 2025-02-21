package com.server.delivery.model.store.repository.operationTimes;

import com.server.delivery.model.store.entity.OperationTimes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class OperationTimesRepositoryImpl implements OperationTimesRepository {
    private final OperationTimesJpaRepository operationTimesJpaRepository;

    @Override
    public List<OperationTimes> saveAll(List<OperationTimes> operationTimesList) {
        return operationTimesJpaRepository.saveAll(operationTimesList);
    }
}
