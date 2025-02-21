package com.server.delivery.model.manager.repository;

import com.server.delivery.model.manager.entity.Manager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ManagerRepositoryImpl implements ManagerRepository {
    private final ManagerJpaRepository managerJpaRepository;

    @Override
    public Manager save(Manager manager) {
        return managerJpaRepository.save(manager);
    }
}
