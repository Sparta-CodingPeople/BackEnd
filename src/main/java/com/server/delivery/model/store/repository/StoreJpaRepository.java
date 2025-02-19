package com.server.delivery.model.store.repository;

import com.server.delivery.model.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreJpaRepository extends JpaRepository<Store, Long> {
}
