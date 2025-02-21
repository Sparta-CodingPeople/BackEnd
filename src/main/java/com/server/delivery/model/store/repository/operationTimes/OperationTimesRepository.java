package com.server.delivery.model.store.repository.operationTimes;

import com.server.delivery.model.store.entity.OperationTimes;

import java.util.List;

public interface OperationTimesRepository {
    List<OperationTimes> saveAll(List<OperationTimes> operationTimesList);
}
