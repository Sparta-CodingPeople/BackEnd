package com.server.delivery.model.store.repository.operationTimes;

import com.server.delivery.model.store.entity.OperationTimes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationTimesJpaRepository extends JpaRepository<OperationTimes, Long> {
}
