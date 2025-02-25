package com.server.delivery.model.menu.repository;

import com.server.delivery.model.menu.entity.Menu;
import com.server.delivery.model.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface MenuRepository {
    Optional<Menu> findByMenuUuId(UUID productUuid);

    Menu save(Menu menu);

    void delete(Menu menu);

    Page<Menu> findByStoreAndMenuNameContainingAndMenuAvailabilityTrue(Store store, String keyword, Pageable pageable);

    Page<Menu> findAllByStoreAndMenuAvailabilityTrue(Store store, Pageable pageable);
}
