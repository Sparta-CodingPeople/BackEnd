package com.server.delivery.model.menu.repository;

import com.server.delivery.model.menu.entity.Menu;
import com.server.delivery.model.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MenuJpaRepository extends JpaRepository<Menu, UUID> {
    Optional<Menu> findByMenuUuId(UUID productId);

    void deleteByMenuUuId(UUID menuUuid);

    Page<Menu> findByStoreAndMenuNameContainingAndMenuAvailabilityTrue(Store store, String keyword, Pageable pageable);
}
