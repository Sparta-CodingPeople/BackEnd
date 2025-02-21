package com.server.delivery.model.store.repository.ownerStore;

import com.server.delivery.model.owner.entity.OwnerStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerStoreJpaRepository extends JpaRepository<OwnerStore, Long> {
}
