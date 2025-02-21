package com.server.delivery.model.manager.repository;

import com.server.delivery.model.manager.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerJpaRepository extends JpaRepository<Manager, Long> {
}
